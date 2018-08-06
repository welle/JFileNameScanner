package aka.media.jfilenamescanner.helpers;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.media.jfilenamescanner.constants.Priority;
import aka.media.jfilenamescanner.constants.Regex;
import aka.media.jfilenamescanner.constants.StringConstants;
import aka.media.jfilenamescanner.utils.FileUtils;
import aka.media.jfilenamescanner.utils.NameMatcher;
import aka.media.jfilenamescanner.utils.TextUtils;
import aka.media.jfilenamescanner.utils.UsualWords;

/**
 * Class TvShowNameMatcher.
 *
 * The purpose of this class is to provide methods to get informations like name of a file or string representing a TV show.
 * To get those informations for a movie, {@link MovieHelper} must be used.
 *
 * @author Charlotte
 */
public class TVShowNameHelper {

    @Nullable
    private File mfile = null;
    @NonNull
    private String filename = StringConstants.EMPTY.getString();
    @NonNull
    private List<@NonNull String> regexs = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param mfile TV show file
     * @param regexs list of regular expression
     * @throws Exception
     */
    public TVShowNameHelper(@NonNull final File mfile, @NonNull final List<@NonNull String> regexs) throws Exception {
        this.mfile = mfile;
        final var name = mfile.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("File name is null or empty.");
        }
        init(name, regexs);
    }

    /**
     * Constructor.
     *
     * @param name TV show name
     * @param regexs list of regular expression
     */
    public TVShowNameHelper(@NonNull final String name, @NonNull final List<@NonNull String> regexs) {
        init(name, regexs);
    }

    private void init(@NonNull final String name, @NonNull final List<@NonNull String> regexList) {
        this.filename = name;
        this.regexs = regexList;
    }

    /**
     * Get TV show name.
     *
     * @return TvShow name or empty string/null if no name found
     */
    @Nullable
    public final String getTvShowName() {
        String toReturn = null;
        // Get all matcher values
        final var names = new ArrayList<@NonNull NameMatcher>();
        getMatcherRes(names, matchByFolderName());
        getMatcherRes(names, matchByEpisode());
        getMatcherRes(names, matchByCommonSeqFileName());
        getMatcherRes(names, matchByRegEx());
        if (names.isEmpty()) {
            final String result = this.filename.substring(0, this.filename.lastIndexOf(StringConstants.DOT.getString()));
            toReturn = UsualWords.standardize(result);
        } else {
            toReturn = UsualWords.matchAllNames(names, true);
        }
        return toReturn;
    }

    private final void getMatcherRes(@NonNull final List<@NonNull NameMatcher> matchResults, @NonNull final NameMatcher tvshowMatcher) {
        if (tvshowMatcher.found()) {
            matchResults.add(tvshowMatcher);
        }
    }

    @NonNull
    private final NameMatcher matchByFolderName() {
        final var folderNameMatcher = new NameMatcher("Folder Name Matcher", Priority.HIGH);
        var res = StringConstants.EMPTY.getString();
        final var currentFile = this.mfile;
        if (currentFile != null) {
            final var parentFile = currentFile.getParent();
            if (parentFile != null) {
                final var pattern = Pattern.compile(Regex.SEASONFOLDERPATTERN.getExpression());
                final var matcher = pattern.matcher(parentFile.substring(parentFile.lastIndexOf(File.separator) + 1));
                if (matcher.find()) {// Parent folder looks like : Season 5,s3,saison 12,...
                    if (currentFile.getParentFile().getParent() != null) {// If parent folder looks like a season folder, parent folder of season folder is probably the TV show name
                        final var current = currentFile.getParentFile().getParentFile();
                        if (current != null) {
                            res = getTvShowFolderName(current);
                        }
                    }
                }
            }
        }
        res = UsualWords.getFilteredName(res, this.regexs);

        folderNameMatcher.setMatch(UsualWords.standardize(res));
        return folderNameMatcher;
    }

    @NonNull
    private final NameMatcher matchByEpisode() {
        final var episodeMatcher = new NameMatcher("Episode Matcher", Priority.MEDIUM);
        var name = this.filename;
        final var pattern = Pattern.compile(Regex.TVSHOWNAMEBYEPISODE.getExpression());
        final var matcher = pattern.matcher(name);

        if (matcher.find()) {// Match episode in fileName
            name = name.substring(0, name.indexOf(matcher.group(0)));
        } else {
            name = StringConstants.EMPTY.getString();
        }

        if (!TextUtils.isEmpty(name)) {
            name = UsualWords.getFilteredName(name, this.regexs);
            episodeMatcher.setMatch(UsualWords.standardize(name));
        }

        return episodeMatcher;
    }

    @NonNull
    private final NameMatcher matchByCommonSeqFileName() {
        final var commonMatcher = new NameMatcher("Common sequence in files matcher", Priority.LOW);
        final var currentFile = this.mfile;
        if (currentFile != null) {
            final var parentFile = currentFile.getParentFile();
            final var files = parentFile.listFiles((FileFilter) file -> {
                var result = false;
                if (file.getName().contains(StringConstants.DOT.getString())) {
                    if (!currentFile.equals(file)) {
                        final var name = file.getName();
                        final var pattern = Pattern.compile(Regex.TVSHOWNAMEBYEPISODE.getExpression());
                        final var matcher = pattern.matcher(name);
                        if (matcher.find()) {
                            result = true;
                        }
                    }
                }
                return result;
            });

            if (files != null && files.length > 1) {
                // Add all words from fileName in list
                final var names = new ArrayList<@NonNull String>();
                for (final File f : files) {
                    final var name = f.getName().substring(0, f.getName().lastIndexOf(StringConstants.DOT.getString()) + 1);
                    names.add(UsualWords.standardize(name));
                }

                // Check if list is as small as possible
                var tvShowNames = UsualWords.getUsualWords(names);
                if (tvShowNames != null) {
                    // Get list as small as possible
                    var tmp = UsualWords.getUsualWords(tvShowNames);
                    while (tmp != null) {
                        tmp = UsualWords.getUsualWords(tmp);
                        if (tmp != null) {
                            tvShowNames = tmp;
                        }
                    }

                    var res = UsualWords.getSmallString(tvShowNames);
                    if (res != null) {
                        res = UsualWords.getFilteredName(res, this.regexs);
                        commonMatcher.setMatch(UsualWords.standardize(res));
                    }
                }
            }
        }

        return commonMatcher;
    }

    @NonNull
    private final NameMatcher matchByRegEx() {
        final var tvshowMatcher = new NameMatcher("Regex Matcher", Priority.MEDIUM);
        var name = this.filename.substring(0, this.filename.lastIndexOf(StringConstants.DOT.getString()));
        name = UsualWords.getFilteredName(name, this.regexs);
        tvshowMatcher.setMatch(UsualWords.standardize(name));
        return tvshowMatcher;
    }

    @NonNull
    private final String getTvShowFolderName(@NonNull final File parentFile) {
        var res = StringConstants.EMPTY.getString();
        if (!FileUtils.isRootDir(parentFile)) {
            final var parent = parentFile.getName().substring(parentFile.getName().lastIndexOf(File.separator) + 1);
            final var pattern = Pattern.compile(Regex.SEASONFOLDERPATTERN.getExpression());
            final var matcher = pattern.matcher(parent);
            if (!matcher.find()) {// Check if folderName is not a tvshowName
                res = parent;
            }
        }

        final var result = res.toLowerCase();
        return result;
    }
}

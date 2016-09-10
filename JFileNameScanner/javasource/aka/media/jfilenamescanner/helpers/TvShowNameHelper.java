package aka.media.jfilenamescanner.helpers;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.media.jfilenamescanner.constants.Priority;
import aka.media.jfilenamescanner.constants.StringConstants;
import aka.media.jfilenamescanner.utils.NameMatcher;
import aka.media.jfilenamescanner.utils.UsualWords;
import aka.swissknife.data.TextUtils;
import aka.swissknife.file.FileUtils;

/**
 * Class TvShowNameMatcher.
 *
 * The purpose of this class is to provide methods to get informations like name of a file or string representing a TV show.
 * To get those informations for a movie, {@link MovieHelper} must be used.
 *
 * @author Charlotte
 */
public class TvShowNameHelper {

    @Nullable
    private File mfile = null;
    @NonNull
    private String filename = StringConstants.EMPTY.getString();
    @NonNull
    private static final String SEASONFOLDERPATTERN = "(?i:season)|(?i:saison)|(?i:s).*\\d+";
    @NonNull
    private static final String TVSHOWFOLDERPATTERN = ".*(?i:tvshwow)|(?i:tv)|(?i:serie)|(?i:série).*";
    @NonNull
    private static final String TVSHOWNAMEBYEPISODE = "(([sS]\\d++\\?\\d++)|(\\d++x\\d++.?\\d++x\\d++)|(\\d++[eE]\\d\\d)|([sS]\\d++.[eE]\\d++)|(\\d++x\\d++)|(\\d++x\\d++.?\\d++\\?\\d++)|(.\\d{3}.))";
    @NonNull
    private List<@NonNull String> regexs = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param mfile TV show file
     * @param regexs list of regular expression
     * @throws Exception if file name can not be retrieved
     */
    public TvShowNameHelper(@NonNull final File mfile, @NonNull final List<@NonNull String> regexs) throws Exception {
        this.mfile = mfile;
        final String name = mfile.getName();
        if (name == null) {
            throw new Exception("File name is null");
        }
        init(name, regexs);
    }

    /**
     * Constructor.
     *
     * @param name TV show name
     * @param regexs list of regular expression
     */
    public TvShowNameHelper(@NonNull final String name, @NonNull final List<@NonNull String> regexs) {
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
        final List<@NonNull NameMatcher> names = new ArrayList<>();
        getMatcherRes(names, matchByFolderName());
        getMatcherRes(names, matchByEpisode());
        getMatcherRes(names, matchByCommonSeqFileName());
        getMatcherRes(names, matchByRegEx());
        if (names.isEmpty()) {
            final String result = this.filename.substring(0, this.filename.lastIndexOf(StringConstants.DOT.getString()));
            assert result != null;
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
        final NameMatcher folderNameMatcher = new NameMatcher("Folder Name Macther", Priority.HIGH);
        String res = StringConstants.EMPTY.getString();
        final File currentFile = this.mfile;
        if (currentFile != null) {
            final String parentFile = currentFile.getParent();
            if (parentFile != null) {
                final Pattern pattern = Pattern.compile(SEASONFOLDERPATTERN);
                final Matcher matcher = pattern.matcher(parentFile.substring(parentFile.lastIndexOf(File.separator) + 1));
                if (matcher.find()) {// Parent folder looks like : Season 5,s3,saison 12,...
                    if (currentFile.getParentFile().getParent() != null) {// If parent folder looks like a season folder, parent folder of season folder is probably the TV show name
                        final File current = currentFile.getParentFile().getParentFile();
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
        final NameMatcher episodeMatcher = new NameMatcher("Episode Matcher", Priority.MEDIUM);
        String name = this.filename;
        final Pattern pattern = Pattern.compile(TVSHOWNAMEBYEPISODE);
        final Matcher matcher = pattern.matcher(name);

        if (matcher.find()) {// Match episode in fileName
            name = name.substring(0, name.indexOf(matcher.group(0)));
        } else {
            name = StringConstants.EMPTY.getString();
        }

        if (!TextUtils.isEmpty(name)) {
            assert name != null;
            name = UsualWords.getFilteredName(name, this.regexs);
            episodeMatcher.setMatch(UsualWords.standardize(name));
        }

        return episodeMatcher;
    }

    @NonNull
    private final NameMatcher matchByCommonSeqFileName() {
        final NameMatcher commonMatcher = new NameMatcher("Common sequence in files matcher", Priority.LOW);
        final File currentFile = this.mfile;
        if (currentFile != null) {
            final File parentFile = currentFile.getParentFile();
            final File[] files = parentFile.listFiles((FileFilter) file -> {
                boolean result = false;
                if (file.getName().contains(StringConstants.DOT.getString())) {
                    if (!currentFile.equals(file)) {
                        final String name = file.getName();
                        final Pattern pattern = Pattern.compile(TVSHOWNAMEBYEPISODE);
                        final Matcher matcher = pattern.matcher(name);
                        if (matcher.find()) {
                            result = true;
                        }
                    }
                }
                return result;
            });

            if (files != null && files.length > 1) {
                // Add all words from fileName in list
                final List<@NonNull String> names = new ArrayList<>();
                for (final File f : files) {
                    final String name = f.getName().substring(0, f.getName().lastIndexOf(StringConstants.DOT.getString()) + 1);
                    names.add(UsualWords.standardize(name));
                }

                // Check if list is as small as possible
                List<@NonNull String> tvShowNames = UsualWords.getUsualWords(names);
                if (tvShowNames != null) {
                    // Get list as small as possible
                    List<@NonNull String> tmp = UsualWords.getUsualWords(tvShowNames);
                    while (tmp != null) {
                        tmp = UsualWords.getUsualWords(tmp);
                        if (tmp != null) {
                            tvShowNames = tmp;
                        }
                    }

                    String res = UsualWords.getSmallString(tvShowNames);
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
        final NameMatcher tvshowMatcher = new NameMatcher("Regex Matcher", Priority.MEDIUM);
        String name = this.filename.substring(0, this.filename.lastIndexOf(StringConstants.DOT.getString()));
        name = UsualWords.getFilteredName(name, this.regexs);
        tvshowMatcher.setMatch(UsualWords.standardize(name));
        return tvshowMatcher;
    }

    @NonNull
    private final String getTvShowFolderName(@NonNull final File parentFile) {
        String res = StringConstants.EMPTY.getString();
        if (!FileUtils.isRootDir(parentFile)) {
            final String parent = parentFile.getName().substring(parentFile.getName().lastIndexOf(File.separator) + 1);
            final Pattern pattern = Pattern.compile(TVSHOWFOLDERPATTERN);
            final Matcher matcher = pattern.matcher(parent);
            if (!matcher.find()) {// Check if folderName is not a tvshowName
                res = parent;
            }
        }

        final String result = res.toLowerCase();
        assert result != null;
        return result;
    }
}

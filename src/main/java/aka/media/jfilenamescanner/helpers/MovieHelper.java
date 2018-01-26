package aka.media.jfilenamescanner.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.media.jfilenamescanner.constants.Priority;
import aka.media.jfilenamescanner.constants.Regex;
import aka.media.jfilenamescanner.constants.StringConstants;
import aka.media.jfilenamescanner.utils.NameMatcher;
import aka.media.jfilenamescanner.utils.TextUtils;
import aka.media.jfilenamescanner.utils.UsualWords;

/**
 * Class MovieHelper.
 *
 * The purpose of this class is to provide methods to get informations like name or year of a file or string representing a movie.
 * To get those informations for a TV show, {@link TVShowNameHelper} or {@link TVShowEpisodeHelper} must be used.
 *
 * @author Charlotte
 */
public final class MovieHelper {

    @NonNull
    private final String filename;
    @Nullable
    private String movieYear;
    @NonNull
    private final List<@NonNull String> regexs;
    @Nullable
    private String movieName;
    private @NonNull final String nameWithoutSuffix;

    /**
     * Constructor.
     *
     * @param mfile movie file.
     * @param regexs list of regular expressions to use.
     * @throws Exception if file name is null or empty
     */
    public MovieHelper(@NonNull final File mfile, @NonNull final List<@NonNull String> regexs) throws Exception {
        final String name = mfile.getName();
        assert name != null : "It should not be possible.";
        this.filename = name;

        final String temp = FilenameUtils.getBaseName(this.filename);
        if (TextUtils.isEmpty(temp)) {
            throw new Exception("File name is null or empty.");
        }

        assert temp != null;
        this.nameWithoutSuffix = temp;
        this.regexs = regexs;
        parseName();
    }

    /**
     * Constructor.
     *
     * @param fileName movie name
     * @param regexs list of regular expressions to use
     * @throws Exception if file name is null or empty
     */
    public MovieHelper(@NonNull final String fileName, @NonNull final List<@NonNull String> regexs) throws Exception {
        this.filename = fileName;

        final String temp = FilenameUtils.getBaseName(this.filename);
        if (TextUtils.isEmpty(temp)) {
            throw new Exception("File name is null or empty.");
        }

        assert temp != null;
        this.nameWithoutSuffix = temp;
        this.regexs = regexs;
        parseName();
    }

    private void parseName() {
        String result;
        // Get all matcher values
        final List<@NonNull NameMatcher> names = new ArrayList<>();
        getMatcherRes(names, getMovieNameByYear(Regex.MOVIENAMEBYYEARW.getExpression()));
        getMatcherRes(names, getMovieNameByYear(Regex.MOVIENAMEBYYEAR.getExpression()));
        getMatcherRes(names, getMovieNameByUpperCase());
        getMatcherRes(names, getMovieNameByRegex());
        if (names.isEmpty()) {
            result = UsualWords.standardize(this.nameWithoutSuffix);
        } else {
            result = UsualWords.matchAllNames(names, false);
            if (result != null) {
                // remove roman number
                result = result.replaceAll(Regex.EPISODE_ROMAN.getExpression(), StringConstants.SPACE.getString());

                // remove number (exemple: Rocky 3 l'oeil du tigre) only if this is not the end of the name
                result = result.replaceAll(Regex.EPISODE_NUMERICAL.getExpression(), StringConstants.SPACE.getString());
                if (result.endsWith(" 1") && !result.toLowerCase().endsWith("part 1") && !result.toLowerCase().endsWith("partie 1")) {
                    result = result.substring(0, result.lastIndexOf(" 1"));
                }
                if (result.endsWith(" I") && !result.toLowerCase().endsWith("part i") && !result.toLowerCase().endsWith("partie i")) {
                    result = result.substring(0, result.lastIndexOf(" I"));
                }

                // at end, remove "et" unusefull
                result = result.replaceAll(Regex.ET.getExpression(), StringConstants.SPACE.getString());
            }
        }
        this.movieName = result;
    }

    /**
     * Get movie name.
     *
     * @return Movie name
     */
    @Nullable
    public String getMovieName() {
        return this.movieName;
    }

    /**
     * Get movie year of the movie.
     *
     * @return year of the movie.
     */
    @Nullable
    public String getYear() {
        return this.movieYear;
    }

    private void getMatcherRes(@NonNull final List<@NonNull NameMatcher> matchResults, @NonNull final NameMatcher nameMatcher) {
        if (nameMatcher.found()) {
            matchResults.add(nameMatcher);
        }
    }

    @NonNull
    private NameMatcher getMovieNameByYear(@NonNull final String stringPattern) {
        final NameMatcher nameMatcher = new NameMatcher("Year Matcher", Priority.MEDIUM);
        String name = null;
        final Pattern pattern = Pattern.compile(stringPattern);
        final Matcher matcher = pattern.matcher(this.filename);
        while (matcher.find()) {
            int indexGrp = 0;
            if (matcher.groupCount() > 1) {
                indexGrp = 1;
            }
            final String syear = matcher.group(indexGrp);
            final int year = Integer.parseInt(trimNonNum(syear));
            if (year >= 1900 && year <= Calendar.getInstance().get(Calendar.YEAR)) {
                final int index = this.filename.indexOf(matcher.group(0));
                name = this.filename.substring(0, index);
                this.movieYear = String.valueOf(year);
            }
        }

        if (!TextUtils.isEmpty(name)) {
            assert name != null;
            name = UsualWords.standardize(name);
            name = UsualWords.getFilteredName(name, this.regexs);
            nameMatcher.setMatch(name);
        }

        return nameMatcher;
    }

    @Nullable
    private String trimNonNum(@Nullable final String text) {
        String result = null;
        if (text != null) {
            result = text.replaceAll("[^0-9]", StringConstants.EMPTY.getString());
        }
        return result;
    }

    @NonNull
    private NameMatcher getMovieNameByUpperCase() {
        final NameMatcher movieMatcher = new NameMatcher("UpperCase Matcher", Priority.LOW);
        String name = UsualWords.standardize(this.nameWithoutSuffix);
        final String[] words = name.split(StringConstants.SPACE.getString());
        String end = null;
        for (final String word : words) {
            final String mword = word.replace(":", StringConstants.EMPTY.getString());
            if (TextUtils.isUpperCase(mword) && !TextUtils.isDigit(mword) && mword.length() > 1) {
                end = word;
                break;
            }
        }

        if (end != null) {
            name = name.substring(0, name.indexOf(end));
            movieMatcher.setMatch(UsualWords.getFilteredName(name, this.regexs));
        }
        return movieMatcher;
    }

    @NonNull
    private NameMatcher getMovieNameByRegex() {
        final NameMatcher movieMatcher = new NameMatcher("Regex Matcher", Priority.MEDIUM);
        @NonNull
        final String name = UsualWords.getFilteredName(UsualWords.standardize(this.nameWithoutSuffix), this.regexs);
        movieMatcher.setMatch(name);
        return movieMatcher;
    }
}

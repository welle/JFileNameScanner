package aka.media.jfilenamescanner.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.media.jfilenamescanner.constants.Priority;
import aka.media.jfilenamescanner.constants.Regex;
import aka.media.jfilenamescanner.constants.StringConstants;
import aka.media.jfilenamescanner.utils.NameMatcher;
import aka.media.jfilenamescanner.utils.UsualWords;
import aka.swissknife.data.TextUtils;

/**
 * Class MovieHelper.
 *
 * The purpose of this class is to provide methods to get informations like name or year of a file or string representing a movie.
 * To get those informations for a TV serie, {@link TvShowNameHelper} or {@link TvShowEpisodeHelper} must be used.
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

    /**
     * Constructor.
     *
     * @param mfile movie file.
     * @param regexs list of regular expressions to use.
     * @throws Exception if file name is null.
     */
    public MovieHelper(@NonNull final File mfile, @NonNull final List<@NonNull String> regexs) throws Exception {
        final String name = mfile.getName();
        if (name == null) {
            throw new Exception("File name null. Can not proceed");
        }
        this.filename = name;
        this.regexs = regexs;
        parseName();
    }

    /**
     * Constructor.
     *
     * @param fileName movie name
     * @param regexs list of regular expressions to use
     */
    public MovieHelper(@NonNull final String fileName, @NonNull final List<@NonNull String> regexs) {
        this.filename = fileName;
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
            final String name = this.filename.substring(0, this.filename.lastIndexOf(StringConstants.DOT.getString()));
            result = UsualWords.standardize(name);
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

    private void getMatcherRes(@NonNull final List<@NonNull NameMatcher> matchResults, @NonNull final NameMatcher movieMatcher) {
        if (movieMatcher.found()) {
            matchResults.add(movieMatcher);
        }
    }

    @NonNull
    private NameMatcher getMovieNameByYear(@NonNull final String stringPattern) {
        final NameMatcher movieMatcher = new NameMatcher("Year Matcher", Priority.MEDIUM);
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

        if (name != null) {
            name = UsualWords.getFilteredName(UsualWords.standardize(name), this.regexs);
            movieMatcher.setMatch(name);
        }

        return movieMatcher;
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
        final String value = this.filename.substring(0, this.filename.lastIndexOf(StringConstants.DOT.getString()));
        String name = UsualWords.standardize(value);
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
        String name = this.filename.substring(0, this.filename.lastIndexOf(StringConstants.DOT.getString()));
        name = UsualWords.getFilteredName(UsualWords.standardize(name), this.regexs);
        movieMatcher.setMatch(name);
        return movieMatcher;
    }
}

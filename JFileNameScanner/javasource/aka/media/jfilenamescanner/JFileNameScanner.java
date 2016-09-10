package aka.media.jfilenamescanner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.media.jfilenamescanner.helpers.MovieHelper;
import aka.media.jfilenamescanner.helpers.TvShowEpisodeHelper;
import aka.media.jfilenamescanner.helpers.TvShowNameHelper;

/**
 * Media scanner name.
 *
 * Will scan given name file of name string to get movie/TV name or year.
 *
 * @author Charlotte
 */
public final class JFileNameScanner {

    @NonNull
    private static final String[] NAME_FILTERS = { "notv", "readnfo", "repack", "proper$", "nfo$", "extended.cut", "limitededition", "limited", "k-sual", "extended", "uncut$", "n° [0-9][0-9][0-9]", "yestv", "stv", "remastered", "limited", "x264", "bluray", "bd5", "bd9", "hddvd", "hdz", "edition.exclusive", "unrated", "walt disney", "dvdrip", "cinefile", "hdmi", "dvd5", "ac3", "culthd", "dvd9", "remux", "edition.platinum", "frenchhqc", "frenchedit", "wawamania", "h264", "bdrip", "brrip",
            "hdteam", "hddvdrip", "subhd", "xvid", "divx", "null$", "divx511", "vorbis", "=str=", "www", "ffm", "mp3", "divx5", "dvb", "mpa2", "blubyte", "brmp", "avs", "filmhd", "hd4u", "1080p", "1080i", "720p", "720i", "720", "truefrench", "dts", "french", "vostfr", "1cd", "2cd", "vff", " vo ", " vf ", "hd", " cam$ ", "telesync", " ts ", " tc ", "ntsc", " pal ", "dvd-r", "dvdscr", "scr$", "r1", "r2", "r3", "r4", "r5", "wp", "subforced", "dvd", "vcd", "avchd", " md", "trailer", "1080p",
            "720p", "MULTI", "x264", "bluray", "festival", "subfrench", "french", "truefrench", "dts", "ac3", "uncut", "theatrical cut", "unrated", "limited", "vostfr", "stv", "rip by", "\\s+rip\\s+", "\\s+by\\s+", "ripped by", "ripped", "version longue", "(version longue)" };

    /**
     * Return the best matching name movie/TV show from file.
     *
     * @param file movie name.
     * @return the movie name scanned.
     * @throws Exception if file name is null.
     */
    @Nullable
    public static String getMovieName(@NonNull final File file) throws Exception {
        String result = null;
        if (file.isFile()) {
            final List<@NonNull String> filters = Arrays.asList(NAME_FILTERS);
            assert filters != null; // as def of list
            final MovieHelper matcher = new MovieHelper(file, filters);
            result = matcher.getMovieName();
        }

        return result;
    }

    /**
     * Return the best matching name movie show from string.
     *
     * @param name movie name.
     * @return movie name scanned.
     */
    @Nullable
    public static String getMovieName(@NonNull final String name) {
        final List<@NonNull String> filters = Arrays.asList(NAME_FILTERS);
        assert filters != null; // as def of list
        final MovieHelper matcher = new MovieHelper(name, filters);

        return matcher.getMovieName();
    }

    /**
     * Get movie year of the movie/TV from file.
     *
     * @param file movie file.
     * @return year of the movie/TV.
     * @throws Exception if file name is null.
     */
    @Nullable
    public static String getMovieYear(@NonNull final File file) throws Exception {
        String result = null;
        if (file.isFile()) {
            final List<@NonNull String> filters = Arrays.asList(NAME_FILTERS);
            assert filters != null; // as def of list
            final MovieHelper matcher = new MovieHelper(file, filters);
            result = matcher.getYear();
        }
        return result;
    }

    /**
     * Get movie year of the movie/TV from name.
     *
     * @param name movie name.
     * @return year of the movie.
     */
    @Nullable
    public static String getMovieYear(@NonNull final String name) {
        final List<@NonNull String> filters = Arrays.asList(NAME_FILTERS);
        assert filters != null; // as def of list
        final MovieHelper matcher = new MovieHelper(name, filters);

        return matcher.getYear();
    }

    /**
     * Get name of the TV show from file name.
     *
     * @param name TV show name.
     * @return name of the TV show.
     */
    @Nullable
    public static String getSerieName(@NonNull final String name) {
        final List<@NonNull String> filters = Arrays.asList(NAME_FILTERS);
        assert filters != null; // as def of list
        final TvShowNameHelper matcher = new TvShowNameHelper(name, filters);

        return matcher.getTvShowName();
    }

    /**
     * Get season of the TV show from file name.
     *
     * @param name TV show file.
     * @return season of the TV show.
     */
    public static int getSerieSeason(@NonNull final String name) {
        final TvShowEpisodeHelper matcher = new TvShowEpisodeHelper(name);

        return matcher.matchEpisode().getSeason();
    }

    /**
     * Get episode of the TV show from file name.
     *
     * @param name TV show name.
     * @return episode of the TV show.
     */
    public static int getSerieEpisode(@NonNull final String name) {
        final TvShowEpisodeHelper matcher = new TvShowEpisodeHelper(name);

        return matcher.matchEpisode().getEpisode();
    }

    /**
     * Get name of the TV show from file.
     *
     * @param file TV show file.
     * @return name of the TV show.
     * @throws Exception if file name can not be retrieved
     */
    @Nullable
    public static String getSerieName(@NonNull final File file) throws Exception {
        final List<@NonNull String> filters = Arrays.asList(NAME_FILTERS);
        assert filters != null; // as def of list
        final TvShowNameHelper matcher = new TvShowNameHelper(file, filters);

        return matcher.getTvShowName();
    }

    /**
     * Get season of the TV show from file.
     *
     * @param file TV show file.
     * @return season of the TV show.
     * @throws Exception if file name can not be retrieved
     */
    public static int getSerieSeason(@NonNull final File file) throws Exception {
        final TvShowEpisodeHelper matcher = new TvShowEpisodeHelper(file);

        return matcher.matchEpisode().getSeason();
    }

    /**
     * Get episode of the TV show from file.
     *
     * @param file TV show file.
     * @return episode of the TV show.
     * @throws Exception if file name can not be retrieved
     */
    public static int getSerieEpisode(@NonNull final File file) throws Exception {
        final TvShowEpisodeHelper matcher = new TvShowEpisodeHelper(file);

        return matcher.matchEpisode().getEpisode();
    }
}

package aka.media.jfilenamescanner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.media.jfilenamescanner.helpers.MovieHelper;
import aka.media.jfilenamescanner.helpers.TVShowEpisodeHelper;
import aka.media.jfilenamescanner.helpers.TVShowNameHelper;

/**
 * Media scanner name.
 *
 * Will scan given name file of name string to get movie/TV name or year.
 *
 * @author Charlotte
 */
public final class JFileNameScanner {

    @NonNull
    private static final List<@NonNull String> NAME_FILTERS = Arrays.asList("notv", "readnfo", "repack", "proper$", "nfo$", "extended.cut", "limitededition", "limited", "k-sual", "extended", "uncut$", "n° [0-9][0-9][0-9]", "yestv", "stv", "remastered", "limited", "x264", "bluray", "bd5", "bd9", "hddvd", "hdz", "edition.exclusive", "unrated", "walt disney", "dvdrip", "cinefile", "hdmi", "dvd5", "ac3", "culthd", "dvd9", "remux", "edition.platinum", "frenchhqc", "frenchedit", "wawamania", "h264",
            "bdrip", "brrip", "hdteam", "hddvdrip", "subhd", "xvid", "divx", "null$", "divx511", "vorbis", "=str=", "www", "ffm", "mp3", "divx5", "dvb", "mpa2", "blubyte", "brmp", "avs", "filmhd", "hd4u", "1080p", "1080i", "720p", "720i", "720", "truefrench", "dts", "french", "vostfr", "1cd", "2cd", "vff", " vo ", " vf ", "hdlight", "hd", " cam$ ", "telesync", " ts ", " tc ", "ntsc", " pal ", "dvd-r", "dvdscr", "scr$", "r1", "r2", "r3", "r4", "r5", "wp", "subforced", "dvd", "vcd", "avchd",
            " md", "redux", "trailer", "1080p", "720p", "MULTI", "x264", "x265", "bluray", "festival", "subfrench", "french", "truefrench", "dts", "ac3", "uncut", "theatrical cut", "unrated", "limited", "vostfr", "stv", "rip by", "\\s+rip\\s+", "\\s+by\\s+", "ripped by", "ripped", "version longue", "(version longue)");

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
            final MovieHelper movieHelper = new MovieHelper(file, NAME_FILTERS);
            result = movieHelper.getMovieName();
        } else {
            throw new Exception(file.getName() + " is not a file, maybe a directory ?");
        }

        return result;
    }

    /**
     * Return the best matching name movie show from string.
     *
     * @param name movie name.
     * @return movie name scanned.
     * @throws Exception if file name is null or empty
     */
    @Nullable
    public static String getMovieName(@NonNull final String name) throws Exception {
        final MovieHelper movieHelper = new MovieHelper(name, NAME_FILTERS);

        return movieHelper.getMovieName();
    }

    /**
     * Get movie year of the movie from file.
     *
     * @param file movie file.
     * @return year of the movie.
     * @throws Exception if file name is null.
     */
    @Nullable
    public static String getMovieYear(@NonNull final File file) throws Exception {
        String result = null;
        if (file.isFile()) {
            final MovieHelper movieHelper = new MovieHelper(file, NAME_FILTERS);
            result = movieHelper.getYear();
        } else {
            throw new Exception(file.getName() + " is not a file, maybe a directory ?");
        }

        return result;
    }

    /**
     * Get movie year of the movie from name.
     *
     * @param name movie name.
     * @return year of the movie.
     * @throws Exception if file name is null or empty
     */
    @Nullable
    public static String getMovieYear(@NonNull final String name) throws Exception {
        final MovieHelper movieHelper = new MovieHelper(name, NAME_FILTERS);

        return movieHelper.getYear();
    }

    /**
     * Get name of the TV show from file name.
     *
     * @param name TV show name.
     * @return name of the TV show.
     */
    @Nullable
    public static String getTVShowName(@NonNull final String name) {
        final TVShowNameHelper tvShowNameHelper = new TVShowNameHelper(name, NAME_FILTERS);

        return tvShowNameHelper.getTvShowName();
    }

    /**
     * Get TV show year of the TV show from name.
     *
     * @param name TV show name.
     * @return year of the TV show.
     * @throws Exception if file name is null or empty
     */
    @Nullable
    public static String getTVShowYear(@NonNull final String name) throws Exception {
        final MovieHelper movieHelper = new MovieHelper(name, NAME_FILTERS);

        return movieHelper.getYear();
    }

    /**
     * Get season of the TV show from file name.
     *
     * @param name TV show file.
     * @return season of the TV show.
     */
    public static int getSeasonOfTVShow(@NonNull final String name) {
        final TVShowEpisodeHelper tvShowEpisodeHelper = new TVShowEpisodeHelper(name);

        return tvShowEpisodeHelper.matchEpisode().getSeason();
    }

    /**
     * Get episode of the TV show from file name.
     *
     * @param name TV show name.
     * @return episode of the TV show.
     */
    public static int getEpisodeOfTVShow(@NonNull final String name) {
        final TVShowEpisodeHelper tvShowEpisodeHelper = new TVShowEpisodeHelper(name);

        return tvShowEpisodeHelper.matchEpisode().getEpisode();
    }

    /**
     * Get name of the TV show from file.
     *
     * @param file TV show file.
     * @return name of the TV show.
     */
    @Nullable
    public static String getTVShowName(@NonNull final File file) {
        final TVShowNameHelper tvShowNameHelper = new TVShowNameHelper(file, NAME_FILTERS);

        return tvShowNameHelper.getTvShowName();
    }

    /**
     * Get season of the TV show from file.
     *
     * @param file TV show file.
     * @return season of the TV show.
     */
    public static int getSeasonOfTVShow(@NonNull final File file) {
        final TVShowEpisodeHelper tvShowEpisodeHelper = new TVShowEpisodeHelper(file);

        return tvShowEpisodeHelper.matchEpisode().getSeason();
    }

    /**
     * Get episode of the TV show from file.
     *
     * @param file TV show file.
     * @return episode of the TV show.
     */
    public static int getEpisodeOfTVShow(@NonNull final File file) {
        final TVShowEpisodeHelper tvShowEpisodeHelper = new TVShowEpisodeHelper(file);

        return tvShowEpisodeHelper.matchEpisode().getEpisode();
    }

    /**
     * Get TV show year of the TV show from file.
     *
     * @param file TV show file.
     * @return year of the TV show.
     * @throws Exception if file name is null or empty
     */
    @Nullable
    public static String getTVShowYear(@NonNull final File file) throws Exception {
        String result = null;
        if (file.isFile()) {
            final MovieHelper movieHelper = new MovieHelper(file, NAME_FILTERS);
            result = movieHelper.getYear();
        } else {
            throw new Exception(file.getName() + " is not a file, maybe a directory ?");
        }
        return result;
    }
}

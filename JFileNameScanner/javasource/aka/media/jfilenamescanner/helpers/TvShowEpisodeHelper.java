package aka.media.jfilenamescanner.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.media.jfilenamescanner.constants.StringConstants;
import aka.media.jfilenamescanner.utils.SeasonXEpisode;
import aka.swissknife.data.TextUtils;

/**
 * Class TvShowEpisodeHelper.
 *
 * The purpose of this class is to provide methods to get informations like name of a file or string representing a TV show.
 * To get those informations for a movie, {@link MovieHelper} must be used.
 *
 * @author Charlotte
 */
public final class TvShowEpisodeHelper {

    private static final Pattern seasonPattern = Pattern.compile("(?:(?:season)|(?:saison)|(?:s))\\W?([0-9]{1,2})");
    private static final Pattern episodePattern = Pattern.compile("(?:(?:(?:[eé]p)|(?:[eé]pisode)) ([0-9]{1,3}))|(?:(?:^| )([0-9]{1,3})[ -_])");

    private enum TvShowPattern {
        SxEPattern("([0-9]{1,2})x([0-9]{1,3})\\D"), SxEPattern2("s([0-9]{1,2}).?[eé]([0-9]{1,3})"), SxEPattern3("(?:^|[\\W} ])([0-9]{1,3})([0-9][0-9])[\\._ \\-]"), SxEPattern4("(?:(?:season)|(?:saison)).?([0-9]{1,2}).*[eé]p.?([0-9]{1,3})"), SxEPattern5("(?:(?:season)|(?:saison)).?([0-9]{1,2}).*(?:[eé]pisode).?([0-9]{1,3})"), SxEPattern6("s([0-9]{1,2}).*[ée]pisode.?\\D?([0-9]{1,3})"), SxEPattern7("([0-9]{2}) ([0-9]{3})(?:\\D|$)");

        @NonNull
        private Pattern pattern;

        private TvShowPattern(@NonNull final String pattern) {
            final Pattern pat = Pattern.compile(pattern);
            this.pattern = pat;
        }

        /**
         * @return pattern
         */
        @NonNull
        public Pattern getPattern() {
            return this.pattern;
        }
    }

    @NonNull
    private String episodeName = StringConstants.EMPTY.getString();
    @Nullable
    private String parentFolder;

    /**
     * Constructor.
     *
     * @param mfile TV show file
     * @throws Exception if file name can not be retrieved
     */
    public TvShowEpisodeHelper(@NonNull final File mfile) throws Exception {
        final String name = mfile.getName();
        if (name == null) {
            throw new Exception("File name is null");
        }
        init(name);
    }

    /**
     * Constructor.
     *
     * @param episodeName TV show name.
     */
    public TvShowEpisodeHelper(@NonNull final String episodeName) {
        init(episodeName);
    }

    private void init(@NonNull final String epName) {
        if (epName.contains(File.separator)) {
            this.parentFolder = epName.substring(0, epName.lastIndexOf(File.separator)).toLowerCase();
            final String name = epName.substring(epName.lastIndexOf(File.separator) + 1);
            assert name != null;
            this.episodeName = standardize(name);
        } else {
            this.episodeName = standardize(epName);
        }
    }

    /**
     * Retrieve season and episode.
     *
     * @return SxE
     */
    @NonNull
    public final SeasonXEpisode matchEpisode() {
        SeasonXEpisode result;
        final List<@NonNull SeasonXEpisode> SxEs = new ArrayList<>();
        SeasonXEpisode sxe;
        for (final TvShowEpisodeHelper.TvShowPattern patternToTest : TvShowEpisodeHelper.TvShowPattern.values()) {
            assert patternToTest != null;
            if ((sxe = match(patternToTest)) != null) {
                SxEs.add(sxe);
            }
        }

        if (SxEs.isEmpty()) {
            sxe = new SeasonXEpisode();
            Matcher matcher = seasonPattern.matcher(this.parentFolder == null ? this.episodeName : this.parentFolder);
            if (matcher.find()) {
                final String season = matcher.group(1);
                sxe.setSeason(TextUtils.isDigit(season) ? Integer.parseInt(season) : 1);
            }

            matcher = episodePattern.matcher(this.episodeName);
            if (matcher.find()) {
                final String episode = matcher.group(1) == null ? matcher.group(2) : matcher.group(1);
                sxe.setEpisode(TextUtils.isDigit(episode) ? Integer.parseInt(episode) : 1);
            }

            if (sxe.isValid()) {
                if (sxe.isPartialyValid()) {
                    if (sxe.getSeason() < 0) {
                        sxe.setSeason(1);
                    }
                    if (sxe.getEpisode() < 0) {
                        sxe.setEpisode(1);
                    }
                }
                result = sxe;
            } else {
                result = new SeasonXEpisode(1, 1);
            }
        } else {
            final List<@NonNull SeasonXEpisode> completeMatch = new ArrayList<>();
            final List<@NonNull SeasonXEpisode> partialMatch = new ArrayList<>();

            // Split complete match and partial match (partial match will be empty in almost all cases)
            for (final SeasonXEpisode match : SxEs) {
                if (match.isValid()) {
                    completeMatch.add(match);
                } else if (match.isPartialyValid()) {
                    partialMatch.add(match);
                }
            }

            // If no complete match, try to make a complete match with partial match
            if (completeMatch.isEmpty() && partialMatch.size() > 1) {
                final SeasonXEpisode match = new SeasonXEpisode();
                for (final SeasonXEpisode mSxE : partialMatch) {
                    if (match.getEpisode() == -1 && mSxE.getEpisode() != -1) {
                        match.setEpisode(mSxE.getEpisode());
                    }
                    if (match.getSeason() == -1 && mSxE.getSeason() != -1) {
                        match.setSeason(mSxE.getSeason());
                    }
                    if (match.isValid()) {
                        break;
                    }
                }
                result = match;
            } else {
                if (completeMatch.size() == 1) {
                    result = completeMatch.get(0);
                } else {
                    // Try to get the most probable match
                    if (completeMatch.size() > 1) {
                        final SeasonXEpisode fMatch = completeMatch.get(0);
                        boolean different = false;
                        for (final SeasonXEpisode match : completeMatch) {
                            if (!fMatch.equals(match)) {
                                different = true;
                            }
                        }
                        if (different) {
                            result = getSxE(completeMatch);
                        } else {
                            result = fMatch;
                        }
                    } else {
                        // No match found
                        if (SxEs.isEmpty() && partialMatch.isEmpty()) {
                            result = new SeasonXEpisode(1, 1);
                        } else {
                            result = partialMatch.isEmpty() ? SxEs.get(0) : partialMatch.get(0);
                        }
                    }
                }
            }
        }

        return result;
    }

    @Nullable
    private final SeasonXEpisode match(@NonNull final TvShowPattern EPpattern) {
        SeasonXEpisode result = null;
        final Matcher matcher = EPpattern.getPattern().matcher(this.episodeName);
        if (matcher.find()) {
            final String season = matcher.group(1);
            final String episode = matcher.group(2);

            int S, E;
            S = TextUtils.isDigit(season) ? Integer.parseInt(season) : -1;
            E = TextUtils.isDigit(episode) ? Integer.parseInt(episode) : -1;

            if (E == 0 && TextUtils.isDigit(season)) {// Absolute number ?
                S = Integer.parseInt(season + episode);
                E = 0;
            }

            if (S != -1 || E != -1) {
                result = new SeasonXEpisode(S, E);
            }
        }
        return result;
    }

    @NonNull
    private final SeasonXEpisode getSxE(@NonNull final List<@NonNull SeasonXEpisode> SxEs) {
        final SeasonXEpisode sxe = new SeasonXEpisode();
        final Map<@NonNull Integer, @NonNull Integer> seasonMatch = new LinkedHashMap<>();
        final Map<@NonNull Integer, @NonNull Integer> episodeMatch = new LinkedHashMap<>();
        for (final SeasonXEpisode tmp : SxEs) {
            final Integer season = Integer.valueOf(tmp.getSeason());
            if (season.intValue() != -1) {
                if (seasonMatch.containsKey(season)) {
                    int count = seasonMatch.get(season).intValue();
                    seasonMatch.remove(season);
                    final Integer value = Integer.valueOf(count++);
                    assert value != null;
                    seasonMatch.put(season, value);
                } else {
                    final Integer value = Integer.valueOf(1);
                    assert value != null;
                    seasonMatch.put(season, value);
                }
            }

            final Integer episode = Integer.valueOf(tmp.getEpisode());
            if (episode.intValue() != -1) {
                if (episodeMatch.containsKey(episode)) {
                    int count = episodeMatch.get(episode).intValue();
                    episodeMatch.remove(episode);
                    final Integer value = Integer.valueOf(count++);
                    assert value != null;
                    episodeMatch.put(season, value);
                } else {
                    final Integer value = Integer.valueOf(1);
                    assert value != null;
                    episodeMatch.put(season, value);
                }
            }

            sxe.setSeason(getMostProbableNumber(seasonMatch));
            sxe.setEpisode(getMostProbableNumber(episodeMatch));
        }
        return sxe;
    }

    private final int getMostProbableNumber(@NonNull final Map<@NonNull Integer, @NonNull Integer> map) {
        int result = -1;
        if (!map.isEmpty()) {
            final Integer key = getKeyByValue(map, Collections.max(map.values()));
            if (key != null) {
                result = key.intValue();
            }
        }
        return result;
    }

    /**
     * Get first key in the map corresponding to given value.
     *
     * @param map map in which the value look
     * @param value value to search
     * @return first key corresponding to value.
     */
    @Nullable
    public final static Integer getKeyByValue(@NonNull final Map<@NonNull Integer, @NonNull Integer> map, @NonNull final Integer value) {
        Integer result = null;
        for (final Entry<@NonNull Integer, @NonNull Integer> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                result = entry.getKey();
                // found result so break
                break;
            }
        }
        return result;
    }

    @NonNull
    private final String standardize(@NonNull final String str) {
        String normalize = str;
        normalize = normalize.substring(0, str.lastIndexOf(StringConstants.DOT.getString()));
        normalize = normalize.replace(StringConstants.DOT.getString(), StringConstants.SPACE.getString()).replace(StringConstants.UNDERSCORE.getString(), StringConstants.SPACE.getString()).replace(StringConstants.DASH.getString(), StringConstants.SPACE.getString()).trim();
        normalize = normalize.replaceAll("[,;:!]", StringConstants.EMPTY.getString());// Remove punctuation
        normalize = normalize.replaceAll("\\s+", StringConstants.SPACE.getString());// Remove duplicate space character

        final String result = str.toLowerCase();
        assert result != null;
        return result;
    }
}

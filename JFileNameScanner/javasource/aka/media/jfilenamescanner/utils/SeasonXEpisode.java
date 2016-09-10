package aka.media.jfilenamescanner.utils;

/**
 * Class SxE.
 *
 * SeasonXEpisode represent an episode from a season.
 *
 * @author Charlotte
 */
public final class SeasonXEpisode {

    private int season;
    private int episode;

    /**
     * Constructor.
     */
    public SeasonXEpisode() {
        this(-1, -1);
    }

    /**
     * Constructor.
     *
     * @param season season number.
     * @param episode episode number.
     */
    public SeasonXEpisode(final int season, final int episode) {
        this.season = season;
        this.episode = episode;
    }

    /**
     * Get the season.
     *
     * @return season.
     */
    public int getSeason() {
        return this.season;
    }

    /**
     * Get the episode.
     *
     * @return episode.
     */
    public int getEpisode() {
        return this.episode;
    }

    /**
     * Check if season and episode are valid.
     *
     * @return <code>true</code> if valid.
     */
    public boolean isValid() {
        return this.season > -1 && this.episode > -1;
    }

    /**
     * Check if season or episode are valid.
     *
     * @return <code>true</code> if partial.
     */
    public boolean isPartialyValid() {
        return this.season != -1 || this.episode != -1;
    }

    /**
     * Set episode.
     *
     * @param episode episode number
     */
    public void setEpisode(final int episode) {
        this.episode = episode;
    }

    /**
     * Set season.
     *
     * @param season season number
     */
    public void setSeason(final int season) {
        this.season = season;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SeasonXEpisode) {
            final SeasonXEpisode sxe = (SeasonXEpisode) obj;
            return sxe.getEpisode() == this.episode && sxe.getSeason() == this.season;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.season;
        hash = 29 * hash + this.episode;
        return hash;
    }
}

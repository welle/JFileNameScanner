package aka.media.jfilenamescanner.constants;

import org.eclipse.jdt.annotation.NonNull;

/**
 * List of used Regex.
 *
 * @author Charlotte
 */
public enum Regex {

    /**
     * Duplicate space character.
     */
    DUPLICATE_SPACE_CHARACTER("\\s+"),

    /**
     * Punctuation.
     */
    PUNCTUATION("[,;:!]"),

    /**
     * Punctuation 2.
     */
    PUNCTUATION2("\\[.*\\]"),

    /**
     * Punctuation 3.
     */
    PUNCTUATION3("\\(.*\\)"),

    /**
     * Useless "ET".
     */
    ET("\\s+([Ee][Tt])+\\s+"),

    /**
     * Movie Year.
     */
    MOVIENAMEBYYEAR("\\D?\\d{4}\\D"),

    /**
     * Movie Year by W.
     */
    MOVIENAMEBYYEARW("\\D?(\\d{4})\\D"),

    /**
     * Episode number numerical.
     */
    EPISODE_NUMERICAL("\\s+(([Ee][Pp][Ii][Ss][Oo][Dd][Ee])?\\s?)?([0-9]+\\s+)+"),

    /**
     * Episode number Roman.
     */
    EPISODE_ROMAN("\\s+(([Ee][Pp][Ii][Ss][Oo][Dd][Ee])?\\s?)?([IXVLCDM]+\\s+)+"),

    /**
     * Season.
     */
    SEASON("(?:(?:season)|(?:saison)|(?:s))\\W?([0-9]{1,2})"),

    /**
     * Episode.
     */
    EPISODE("(?:(?:(?:[eé]p)|(?:[eé]pisode)) ([0-9]{1,3}))|(?:(?:^| )([0-9]{1,3})[ -_])"),

    /**
     * Season folder pattern.
     */
    SEASONFOLDERPATTERN("(?i:season)|(?i:saison)|(?i:s).*\\d+"),

    /**
     * TV show folder pattern.
     */
    TVSHOWFOLDERPATTERN(".*(?i:tvshwow)|(?i:tv)|(?i:serie)|(?i:série).*"),

    /**
     * TV show name by episode.
     */
    TVSHOWNAMEBYEPISODE("(([sS]\\d++\\?\\d++)|(\\d++x\\d++.?\\d++x\\d++)|(\\d++[eE]\\d\\d)|([sS]\\d++.[eE]\\d++)|(\\d++x\\d++)|(\\d++x\\d++.?\\d++\\?\\d++)|(.\\d{3}.))"),

    /**
     * Empty string.
     */
    EMPTY_STRING("");

    @NonNull
    private String expression;

    private Regex(@NonNull final String expression) {
        this.expression = expression;
    }

    /**
     * Get expression.
     *
     * @return expression
     */
    @NonNull
    public String getExpression() {
        return this.expression;
    }
}

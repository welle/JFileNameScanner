package aka.media.jfilenamescanner.constants;

import org.eclipse.jdt.annotation.NonNull;

/**
 * List of used string.
 *
 * @author Charlotte
 */
public enum StringConstants {

    /**
     * Empty string.
     */
    EMPTY(""),
    /**
     * Dot.
     */
    DOT("."),
    /**
     * Dash.
     */
    DASH("-"),
    /**
     * Underscore.
     */
    UNDERSCORE("_"),
    /**
     * Space.
     */
    SPACE(" ");

    @NonNull
    private String string;

    private StringConstants(@NonNull final String string) {
        this.string = string;
    }

    /**
     * Get string.
     *
     * @return string
     */
    @NonNull
    public String getString() {
        return this.string;
    }
}

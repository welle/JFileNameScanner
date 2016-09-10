package aka.media.jfilenamescanner.constants;

import org.eclipse.jdt.annotation.NonNull;

/**
 * List of matcher names.
 *
 * @author Charlotte
 */
public enum MatcherNameConstants {

    /**
     * Empty string.
     */
    EMPTY(""),
    /**
     * Space.
     */
    SPACE(" ");

    @NonNull
    private String string;

    private MatcherNameConstants(@NonNull final String string) {
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

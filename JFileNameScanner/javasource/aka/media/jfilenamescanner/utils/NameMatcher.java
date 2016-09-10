package aka.media.jfilenamescanner.utils;

import org.eclipse.jdt.annotation.NonNull;

import aka.media.jfilenamescanner.constants.Priority;
import aka.media.jfilenamescanner.constants.StringConstants;

/**
 * Class NameMatcher.
 *
 * @author Charlotte
 */
public final class NameMatcher {

    @NonNull
    private final Priority priority;
    @NonNull
    private final String name;
    @NonNull
    private String result;

    /**
     * Constructor.
     *
     * @param name Matcher name.
     * @param priority Matcher priority.
     */
    public NameMatcher(@NonNull final String name, @NonNull final Priority priority) {
        this.name = name;
        this.priority = priority;
        this.result = StringConstants.EMPTY.getString();
    }

    /**
     * Get the name.
     *
     * @return name.
     */
    @NonNull
    public String getName() {
        return this.name;
    }

    /**
     * Get the match.
     *
     * @return result.
     */
    @NonNull
    public String getMatch() {
        return this.result;
    }

    /**
     * Set match.
     *
     * @param result set the match
     */
    public void setMatch(@NonNull final String result) {
        if (result.length() > 0) {
            this.result = result;
        }
    }

    /**
     * Get the priority.
     *
     * @return priority.
     */
    @NonNull
    public Priority getPriority() {
        return this.priority;
    }

    /**
     * Check if result founded.
     *
     * @return <code>true</code> if result founded.
     */
    public boolean found() {
        return this.result.length() > 0;
    }
}

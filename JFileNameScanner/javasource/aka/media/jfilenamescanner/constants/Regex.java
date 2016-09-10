package aka.media.jfilenamescanner.constants;

import org.eclipse.jdt.annotation.NonNull;

/**
 * List of used Regex.
 *
 * @author Charlotte
 */
public enum Regex {

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

package aka.media.jfilenamescanner.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Text utils.
 *
 * @author Charlotte
 */
public final class TextUtils {

    @NonNull
    private static List<@NonNull String> ISOLANGUAGE_STRING_LIST;

    static {
        ISOLANGUAGE_STRING_LIST = new ArrayList<>();
        final String[] localeArray = Locale.getISOLanguages();
        for (final String locale : localeArray) {
            ISOLANGUAGE_STRING_LIST.add(locale.toString());
        }
    }

    private TextUtils() {
        // private constructor, to ensure the class can not be instantiated
    }

    /**
     * @param text
     * @return true if passed String is null or empty
     */
    public static boolean isEmpty(final String text) {
        return text == null || text.trim().length() == 0;
    }

    /**
     * @param text
     * @return true if passed CharSequence is null or empry
     */
    public static boolean isEmpty(final CharSequence text) {
        return text == null || text.length() == 0;
    }

    /**
     * @param text
     * @return String based on the CharSequence but with all white space removed
     */
    @NonNull
    public static String pack(@Nullable final CharSequence text) {
        if (text == null) {
            return "";
        }
        final var length = text.length();
        final var sb = new StringBuilder(text.length());
        for (int i = 0; i < length; ++i) {
            final char chr = text.charAt(i);
            if (!Character.isWhitespace(chr)) {
                sb.append(chr);
            }
        }
        final var result = sb.toString();
        return result;
    }

    /**
     * @param text
     * @return String with all alphanum removed
     */
    @Nullable
    public static String trimNonAlphaNum(final String text) {
        String result = null;
        if (text != null) {
            result = text.replaceAll("[^a-zA-Z0-9]", "");
        }
        return result;
    }

    /**
     * @param text
     * @return true if CharSequence contains whitespace
     */
    public static boolean containsWhitespace(final CharSequence text) {
        var result = false;
        if (text != null) {
            final var length = text.length();
            for (var i = 0; i < length; ++i) {
                final var chr = text.charAt(i);
                if (Character.isWhitespace(chr)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Try to capitalize the first character.
     *
     * @param word
     * @return the word capitalized if it is possible
     */
    @NonNull
    public static CharSequence capitalizeWord(@NonNull final CharSequence word) {
        if (isEmpty(word)) {
            return word;
        }
        final var tmp = new StringBuilder(word);
        final var firstChr = tmp.charAt(0);
        tmp.setCharAt(0, Character.toUpperCase(firstChr));
        return tmp.subSequence(0, tmp.length());
    }

    /**
     * @param strings
     * @return packed strings
     */
    @NonNull
    public static String pack(final String @Nullable... strings) {
        final var sb = new StringBuilder();

        if (strings != null && strings.length > 0) {
            for (int i = 0; i < strings.length; i++) {
                final var temp = strings[i];
                sb.append(temp);
            }
        }

        final var result = sb.toString();
        return result;
    }

    /**
     * Check if string is uppercase
     *
     * @param str
     * @return True if all letter are uppercase except I,II,III,..., false otherwise
     */
    public static boolean isUpperCase(final @NonNull String str) {
        final var romanNumber = new String[] { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
        for (final String number : romanNumber) {
            if (str.equals(number)) {
                return false;
            }
        }
        for (int i = 0; i < str.length(); i++) {
            final var ch = str.charAt(i);
            if (ch < 32 || ch > 96) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if string is a digit
     *
     * @param str String
     * @return True if str is a digit, false otherwise
     */
    public static boolean isDigit(final @Nullable String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if string can be stored in database
     *
     * @param s
     * @return <code>true</code> if string can be stored in sql
     */
    public static boolean isUTF8(final @Nullable String s) {
        /**
         * <pre>
         * ^   # start of subject
         *     (?:     # match this:
         *         [           # match a:
         *             \p{L}       # Unicode letter, or
         *             \p{Mn}      # Unicode accents, or
         *             \p{Pd}      # Unicode hyphens, or
         *             \'          # single quote, or
         *         ]+              # one or more times
         *         \s          # any kind of space
         *         [               #match a:
         *             \p{L}       # Unicode letter, or
         *             \p{Mn}      # Unicode accents, or
         *             \p{Pd}      # Unicode hyphens, or
         *             \'          # single quote, or
         *         ]+              # one or more times
         *         \s?         # any kind of space (0 or more times)
         *     )+      # one or more times
         * $   # end of subject
         * </pre>
         */
        final var pattern = "^(?:[\\p{L}\\p{Mn}\\p{Pd}\\']+\\s[\\p{L}\\p{Mn}\\p{Pd}\\']+\\s?)+$";
        return Pattern.compile(pattern).matcher(s).matches();
    }

    /**
     * Decode a UTF-16 String
     *
     * @param num the UTF-16 string to decode
     * @return the resulting string
     */
    @Nullable
    public final static String encode_utf16F4(final @NonNull String num) {
        String stringISO = null;
        try {
            stringISO = new String(num.getBytes(), "ISO-8859-1");
        } catch (final UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringISO;
    }

    /**
     * Decode a UTF-16 String
     *
     * @param num the UTF-16 string to decode
     * @return the resulting string
     */
    @Nullable
    public final static String decode_utf16F4(final @NonNull String num) {
        String stringISO = null;
        try {
            stringISO = new String(num.getBytes(), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringISO;
    }

    /**
     * remove all non-numerical values from the given string
     *
     * @param text
     * @return concatenated string
     */
    @Nullable
    public static String trimNonNumerical(@Nullable final String text) {
        String result = null;
        if (text != null) {
            result = text.replaceAll("[^0-9]", "");
        }
        return result;
    }

}

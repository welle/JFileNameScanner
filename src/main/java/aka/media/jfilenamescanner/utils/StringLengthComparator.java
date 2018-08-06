package aka.media.jfilenamescanner.utils;

import java.util.Comparator;

import aka.media.jfilenamescanner.constants.StringConstants;

/**
 * Class StringLengthComparable.
 *
 * Compare two string length.
 *
 * @author Charlotte
 */
public final class StringLengthComparator implements Comparator<String> {

    @Override
    public int compare(final String s1, final String s2) {
        var string1 = s1;
        if (s1 == null) {
            string1 = StringConstants.EMPTY.getString();
        }
        var string2 = s2;
        if (string2 == null) {
            string2 = StringConstants.EMPTY.getString();
        }

        if (string1.length() == 2) {
            return 1;
        }
        if (string2.length() == 2) {
            return -1;
        }
        return string1.length() - string2.length();
    }
}

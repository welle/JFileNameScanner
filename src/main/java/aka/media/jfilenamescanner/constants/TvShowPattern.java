package aka.media.jfilenamescanner.constants;

import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;

/**
 * TV Show Pattern.
 *
 * @author Cha
 */
public enum TvShowPattern {

    /**
     * <ul>
     * <li>1st Capturing group ([0-9]{1,2})
     * <ul>
     * <li>[0-9]{1,2} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,2} Between 1 and 2 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>x matches the character x literally (case sensitive)</li>
     * <li>2nd Capturing group ([0-9]{1,3})
     * <ul>
     * <li>[0-9]{1,3} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,3} Between 1 and 3 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>\\ matches the character \ literally</li>
     * <li>D matches the character D literally (case sensitive)</li>
     * </ul>
     */
    SxEPattern("([0-9]{1,2})x([0-9]{1,3})\\D"),

    /**
     * <ul>
     * <li>s matches the character s literally (case sensitive)</li>
     * <li>1st Capturing group ([0-9]{1,2})
     * <ul>
     * <li>[0-9]{1,2} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,2} Between 1 and 2 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>.? matches any character (except newline)
     * <ul>
     * <li>Quantifier: ? Between zero and one time, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>[e�] match a single character present in the list below
     * <ul>
     * <li>e� a single character in the list e� literally (case sensitive)</li>
     * </ul>
     * </li>
     * <li>2nd Capturing group ([0-9]{1,3})
     * <ul>
     * <li>[0-9]{1,3} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,3} Between 1 and 3 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * </ul>
     */
    SxEPattern2("s([0-9]{1,2}).?[e�]([0-9]{1,3})"),

    /**
     * <ul>
     * <li>(?:^|[\\W} ]) Non-capturing group
     * <ul>
     * <li>1st Alternative: ^
     * <ul>
     * <li>^ assert position at start of the string</li>
     * </ul>
     * </li>
     * <li>2nd Alternative: [\\W} ]
     * <ul>
     * <li>[\\W} ] match a single character present in the list below
     * <ul>
     * <li>\\ matches the character \ literally</li>
     * <li>W} a single character in the list W} literally (case sensitive)</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>1st Capturing group ([0-9]{1,3})
     * <ul>
     * <li>[0-9]{1,3} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,3} Between 1 and 3 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>2nd Capturing group ([0-9][0-9])
     * <ul>
     * <li>[0-9] match a single character present in the list below
     * <ul>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * <li>[0-9] match a single character present in the list below
     * <ul>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>[\\._ \\-] match a single character present in the list below
     * <ul>
     * <li>\\ matches the character \ literally</li>
     * <li>._ a single character in the list ._ literally</li>
     * <li>\\ matches the character \ literally</li>
     * <li>- the literal character -</li>
     * </ul>
     * </li>
     * </ul>
     */
    SxEPattern3("(?:^|[\\W} ])([0-9]{1,3})([0-9][0-9])[\\._ \\-]"),

    /**
     * <ul>
     * <li>(?:(?:season)|(?:saison)) Non-capturing group
     * <ul>
     * <li>1st Alternative: (?:season)
     * <ul>
     * <li>(?:season) Non-capturing group
     * <ul>
     * <li>season matches the characters season literally (case sensitive)</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>2nd Alternative: (?:saison)
     * <ul>
     * <li>(?:saison) Non-capturing group
     * <ul>
     * <li>saison matches the characters saison literally (case sensitive)</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>.? matches any character (except newline)
     * <ul>
     * <li>Quantifier: ? Between zero and one time, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>1st Capturing group ([0-9]{1,2})
     * <ul>
     * <li>[0-9]{1,2} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,2} Between 1 and 2 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>.* matches any character (except newline)
     * <ul>
     * <li>Quantifier: * Between zero and unlimited times, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>[e�] match a single character present in the list below
     * <ul>
     * <li>e� a single character in the list e� literally (case sensitive)</li>
     * </ul>
     * </li>
     * <li>p matches the character p literally (case sensitive)</li>
     * <li>.? matches any character (except newline)
     * <ul>
     * <li>Quantifier: ? Between zero and one time, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>2nd Capturing group ([0-9]{1,3})
     * <ul>
     * <li>[0-9]{1,3} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,3} Between 1 and 3 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * </ul>
     */
    SxEPattern4("(?:(?:season)|(?:saison)).?([0-9]{1,2}).*[e�]p.?([0-9]{1,3})"),

    /**
     * <ul>
     * <li>(?:(?:season)|(?:saison)) Non-capturing group
     * <ul>
     * <li>1st Alternative: (?:season)
     * <ul>
     * <li>(?:season) Non-capturing group
     * <ul>
     * <li>season matches the characters season literally (case sensitive)</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>2nd Alternative: (?:saison)
     * <ul>
     * <li>(?:saison) Non-capturing group
     * <ul>
     * <li>saison matches the characters saison literally (case sensitive)</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>.? matches any character (except newline)
     * <ul>
     * <li>Quantifier: ? Between zero and one time, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>1st Capturing group ([0-9]{1,2})
     * <ul>
     * <li>[0-9]{1,2} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,2} Between 1 and 2 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>.* matches any character (except newline)
     * <ul>
     * <li>Quantifier: * Between zero and unlimited times, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>(?:[e�]pisode) Non-capturing group
     * <ul>
     * <li>[e�] match a single character present in the list below
     * <ul>
     * <li>e� a single character in the list e� literally (case sensitive)</li>
     * </ul>
     * </li>
     * <li>pisode matches the characters pisode literally (case sensitive)</li>
     * </ul>
     * </li>
     * <li>.? matches any character (except newline)
     * <ul>
     * <li>Quantifier: ? Between zero and one time, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>2nd Capturing group ([0-9]{1,3})
     * <ul>
     * <li>[0-9]{1,3} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,3} Between 1 and 3 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * </ul>
     */
    SxEPattern5("(?:(?:season)|(?:saison)).?([0-9]{1,2}).*(?:[e�]pisode).?([0-9]{1,3})"),

    /**
     * <ul>
     * <li>s matches the character s literally (case sensitive)</li>
     * <li>1st Capturing group ([0-9]{1,2})
     * <ul>
     * <li>[0-9]{1,2} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,2} Between 1 and 2 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>.* matches any character (except newline)
     * <ul>
     * <li>Quantifier: * Between zero and unlimited times, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>[�e] match a single character present in the list below
     * <ul>
     * <li>�e a single character in the list �e literally (case sensitive)</li>
     * </ul>
     * </li>
     * <li>pisode matches the characters pisode literally (case sensitive)</li>
     * <li>.? matches any character (except newline)
     * <ul>
     * <li>Quantifier: ? Between zero and one time, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>\\ matches the character \ literally</li>
     * <li>D? matches the character D literally (case sensitive)
     * <ul>
     * <li>Quantifier: ? Between zero and one time, as many times as possible, giving back as needed</li>
     * </ul>
     * </li>
     * <li>2nd Capturing group ([0-9]{1,3})
     * <ul>
     * <li>[0-9]{1,3} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {1,3} Between 1 and 3 times, as many times as possible, giving back as needed</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * </ul>
     */
    SxEPattern6("s([0-9]{1,2}).*[�e]pisode.?\\D?([0-9]{1,3})"),

    /**
     * <ul>
     * <li>1st Capturing group ([0-9]{2})
     * <ul>
     * <li>[0-9]{2} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {2} Exactly 2 times</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>matches the character literally</li>
     * <li>2nd Capturing group ([0-9]{3})
     * <ul>
     * <li>[0-9]{3} match a single character present in the list below
     * <ul>
     * <li>Quantifier: {3} Exactly 3 times</li>
     * <li>0-9 a single character in the range between 0 and 9</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * <li>(?:\\D|$) Non-capturing group
     * <ul>
     * <li>1st Alternative: \\D
     * <ul>
     * <li>\\ matches the character \ literally</li>
     * <li>D matches the character D literally (case sensitive)</li>
     * </ul>
     * </li>
     * <li>2nd Alternative: $
     * <ul>
     * <li>$ assert position at end of the string</li>
     * </ul>
     * </li>
     * </ul>
     * </li>
     * </ul>
     */
    SxEPattern7("([0-9]{2}) ([0-9]{3})(?:\\D|$)");

    @NonNull
    private Pattern pattern;

    private TvShowPattern(@NonNull final String pattern) {
        final Pattern pat = Pattern.compile(pattern);
        this.pattern = pat;
    }

    /**
     * Return the current pattern.
     *
     * @return pattern
     */
    @NonNull
    public Pattern getPattern() {
        return this.pattern;
    }
}

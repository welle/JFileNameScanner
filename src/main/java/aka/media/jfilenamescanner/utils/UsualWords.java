package aka.media.jfilenamescanner.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.media.jfilenamescanner.constants.Priority;
import aka.media.jfilenamescanner.constants.Regex;
import aka.media.jfilenamescanner.constants.StringConstants;

/**
 * CommonWords
 *
 * @author Charlotte
 */
public abstract class UsualWords {

    /**
     * Match all result from all matcher.
     *
     * @param names Matchers results
     * @param priority priority
     * @return Most probable result
     */
    @Nullable
    public static final String matchAllNames(@NonNull final List<@NonNull NameMatcher> names, final boolean priority) {
        String result = null;
        if (names.size() == 1) {
            result = standardize(names.get(0).getMatch());
        } else {
            if (priority) {
                for (final NameMatcher name : names) {
                    if (name.getPriority() == Priority.HIGH) {
                        result = standardize(name.getMatch());
                    }
                }
            } else {
                final var allMatch = names.stream().map(n -> n.getMatch()).collect(Collectors.toList());
                var movieNames = getUsualWords(allMatch);
                if (movieNames == null || movieNames.isEmpty()) {
                    result = getSmallString(allMatch);
                } else {
                    var tmp = getUsualWords(movieNames);
                    while (tmp != null) {
                        tmp = getUsualWords(tmp);
                        if (tmp != null) {
                            movieNames = tmp;
                        }
                    }

                    result = getSmallString(movieNames);
                }
            }
        }

        return result;
    }

    /**
     * Get list of common words in list of string separated by space character.
     *
     * @param names name List
     * @return List of common words or null if list is as small as possible
     */
    @Nullable
    public final static List<@NonNull String> getUsualWords(@NonNull final List<@NonNull String> names) {
        var common = new ArrayList<@NonNull String>();
        for (int i = 0; i < names.size(); i++) {
            for (int j = 0; j < names.size(); j++) {
                if (i != j) {
                    final var list1 = List.of(names.get(i).toLowerCase().split(StringConstants.SPACE.getString()));
                    final var list2 = List.of(names.get(i).toLowerCase().split(StringConstants.SPACE.getString()));
                    final String res = getCommonList(list1, list2);
                    if (res.length() > 0) {
                        common.add(standardize(res));
                    }
                }
            }
        }

        if (names.size() == 1) {
            common.add(standardize(names.get(0)));
        }

        final var set = new HashSet<>(common);
        common = new ArrayList<>(set);

        Collections.sort(names);
        Collections.sort(common);

        if (names.equals(common)) {
            return null;
        }

        return common;
    }

    /**
     * Get common words between two string list.
     *
     * @param list1 String list
     * @param list2 String list
     * @return String with all common words separated by space character or empty string
     */
    @NonNull
    private static final String getCommonList(@NonNull final List<String> list1, @NonNull final List<String> list2) {
        final var sb = new StringBuilder();
        for (final String str : list1) {
            if (list2.contains(str)) {
                if (sb.length() != 0) {
                    sb.append(StringConstants.SPACE.getString());
                }
                sb.append(str);
            }
        }

        final var result = sb.toString().trim();
        return result;
    }

    /**
     * Standardize string.
     *
     * @param str string to normalized
     * @return String normalized
     */
    @NonNull
    public static final String standardize(@NonNull final String str) {
        var normalize = str;
        normalize = normalize.replace(StringConstants.DOT.getString(), StringConstants.SPACE.getString()).replace(StringConstants.UNDERSCORE.getString(), StringConstants.SPACE.getString()).replace(StringConstants.DASH.getString(), StringConstants.SPACE.getString()).trim();
        normalize = normalize.replaceAll(Regex.PUNCTUATION.getExpression(), StringConstants.EMPTY.getString());
        normalize = normalize.replaceAll(Regex.PUNCTUATION2.getExpression(), StringConstants.EMPTY.getString()).replaceAll(Regex.PUNCTUATION3.getExpression(), StringConstants.EMPTY.getString());
        normalize = normalize.replaceAll(Regex.DUPLICATE_SPACE_CHARACTER.getExpression(), StringConstants.SPACE.getString());
        final var result = normalize.trim();
        return result;
    }

    /**
     * Get the smallest string in list.
     *
     * @param list list of string
     * @return Smallest string if exist
     */
    @Nullable
    public static final String getSmallString(@NonNull final List<@NonNull String> list) {
        String result = null;
        if (list.size() > 0) {
            Collections.sort(list, new StringLengthComparator());
            result = standardize(list.get(0));
        }

        return result;
    }

    /**
     * Replace all regular expression from list in media name by void, no case sensitive.
     *
     * @param mediaName Media name to filtered
     * @param replaceBy List of regular expression
     * @return Cleaned movie name by regex
     */
    @NonNull
    public static final String getFilteredName(@NonNull final String mediaName, @NonNull final List<@NonNull String> replaceBy) {
        var res = mediaName.replaceAll("\\.", StringConstants.SPACE.getString());
        res = res.replaceAll(StringConstants.UNDERSCORE.getString(), StringConstants.SPACE.getString());
        for (final String regex : replaceBy) {
            res = res.replaceAll("(?i)" + regex, StringConstants.EMPTY.getString());
        }

        return res;
    }
}

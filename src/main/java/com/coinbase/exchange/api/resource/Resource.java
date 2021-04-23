package com.coinbase.exchange.api.resource;

import com.coinbase.exchange.util.Format;
import lombok.Getter;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enum containing information on resource authority. Each {@link Resource} is defined by its resource URI format and
 * the expected argument count. Currently there is only support for static argument counts (no optional args).
 */
public enum Resource {

    /**
     * Accounts API resources.
     */
    LIST_ACCOUNTS           ("/accounts",               expects(0)),
    GET_ACCOUNT             ("/accounts/{0}",           expects(1)),
    GET_ACCOUNT_HISTORY     ("/accounts/{0}/ledger",    expects(1)),
    GET_HOLDS               ("/accounts/{0}/ledger",    expects(1));

    /**
     * TODO API resources
     */

    // resource URI format
    @Getter private final String uri;

    // args count range
    private final Range<Integer> argsRange;

    /**
     * Initialize resource fields and validate the URI format and args range is valid.
     *
     * @param uri URI format
     * @param argsRange Expected args range
     */
    Resource(final String uri, final Range<Integer> argsRange) {
        this.uri = uri;
        this.argsRange = argsRange;
        validate();
    }

    /**
     * Gets the maximum expected args count, which is the upper bound of its args range.
     *
     * @return Maximum expected args, inclusive.
     */
    public int getMaxArgs() {
        return argsRange.getMaximum();
    }

    /**
     * Checks that the input count is a valid number of args.
     *
     * @param count Args count to test
     * @return True if provided a valid args count, false otherwise
     */
    public boolean isValidArgsCount(final int count) {
        return argsRange.contains(count);
    }

    /**
     * Checks that the number of input args is valid.
     *
     * @param args Args
     * @return True if provided a valid number of args, false otherwise
     */
    public boolean isValidArgsCount(final Object ... args) {
        return isValidArgsCount(args.length);
    }

    /**
     * Formats URI based on the provided args array. Performs validation on args beforehand. If the validation check
     * fails, an {@link IllegalArgumentException} is thrown.
     *
     * @param args Args
     * @return Formatted URI
     */
    public String populateUri(final Object[] args) {
        if (isValidArgsCount(args)) {
            return Format.format(uri, args);
        }
        throw new IllegalArgumentException(Format.format("Invalid args count for URI: [{}], args: {}",
                uri, Arrays.toString(args)));
    }

    /**
     * Validation check on format URI and expected args range. Ensures the correct number of delimiters are present
     * and that every open bracket has a matching closing bracket.
     *
     * <p> Current only accepts delimiters: <code>{}</code> and <code>{x}</code>, where <code>x</code> is an integer.
     */
    private void validate() {
        final int openBracketCount = StringUtils.countMatches("{", uri);
        final int closingBracketCount = StringUtils.countMatches("}", uri);

        // validate bracket counts
        if (openBracketCount != closingBracketCount) {
            throw new IllegalStateException(Format.format(
                    "Invalid bracket arrangement for URI [{}]. Open brackets: {}, closing brackets: {}",
                    uri, openBracketCount, closingBracketCount));
        }

        // validate delimiter formatting
        final Matcher delimiterFormat = Pattern.compile("\\{\\d}").matcher(uri);
        if (delimiterFormat.results().count() != openBracketCount) {
            throw new IllegalStateException(Format.format(
                    "Invalid bracket arrangement for URI [{}].", uri));
        }

        // validate URI expected args count matches input range
        if (!isValidArgsCount(openBracketCount)) {
            throw new IllegalStateException(Format.format(
                    "Expected args range {} does not match format URI: [{}]", argsRange, uri));
        }
    }

    /**
     * Creates range between <code>i</code> and <code>j</code> inclusive.
     *
     * @param i Beginning of range
     * @param j Ending of range
     * @return {@link Range} of <code>i</code> and <code>j</code>
     */
    private static Range<Integer> range(final int i, final int j) {
        return Range.between(i, j);
    }

    /**
     * Creates range that contains just 1 value <code>i</code>.
     *
     * @param i Beginning and end of range
     * @return {@link Range} of just <code>i</code>
     */
    private static Range<Integer> expects(final int i) {
        return Range.is(i);
    }
}

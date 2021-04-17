package com.coinbase.api.resource;

import com.coinbase.util.Format;
import lombok.Getter;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum Resource {
    /**
     * Accounts API resources.
     */
    LIST_ACCOUNTS           ("/accounts",               expects(0)),
    GET_ACCOUNT             ("/accounts/{0}",           expects(1)),
    GET_ACCOUNT_HISTORY     ("/accounts/{0}/ledger",    expects(1)),
    GET_HOLDS               ("/accounts/{0}/ledger",    expects(1));

    /**
     * Orders API resources.
     */

    /**
     * TODO API resources
     */

    // resource uri format
    @Getter private final String uri;
    private final Range<Integer> argsRange;

    Resource(final String uri, final Range<Integer> argsRange) {
        this.uri = uri;
        this.argsRange = argsRange;
        validate();
    }

    public int getMaxArgs() {
        return argsRange.getMaximum();
    }

    public boolean isValidArgsCount(final int count) {
        return argsRange.contains(count);
    }

    public boolean isValidArgsCount(final Object ... args) {
        return isValidArgsCount(args.length);
    }

    public String populateUri(final Object[] args) {
        if (isValidArgsCount(args)) {
            return Format.format(uri, args);
        }
        throw new IllegalArgumentException(Format.format("Invalid args count for URI: [{}], args: {}",
                uri, Arrays.toString(args)));
    }

    private void validate() {
        final int openBracketCount = StringUtils.countMatches("{", uri);
        final int closingBracketCount = StringUtils.countMatches("}", uri);

        // validate bracket counts
        if (openBracketCount != closingBracketCount) {
            throw new IllegalStateException(Format.format(
                    "Invalid bracket arrangement for URI [{}]. Open brackets: {}, closing brackets: {}",
                    uri, openBracketCount, closingBracketCount));
        }

        // validate URI expected args count matches input range
        if (!isValidArgsCount(openBracketCount)) {
            throw new IllegalStateException(Format.format(
                    "Expected args range {} does not match format URI: [{}]", argsRange, uri));
        }
    }

    private static Range<Integer> range(int i, int j) {
        return Range.between(i, j);
    }

    private static Range<Integer> expects(int i) {
        return Range.is(i);
    }
}

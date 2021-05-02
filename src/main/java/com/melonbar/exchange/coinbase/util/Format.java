package com.melonbar.exchange.coinbase.util;

import org.slf4j.helpers.MessageFormatter;

/**
 * Basic utility class for formatting messages with a variable count of arguments. Currently this class relies on
 * SLF4J's implementation of {@link MessageFormatter} as the underlying implementation for argument substitution.
 *
 * <p> Such a dependency on a logging framework's formatter is not ideal, but for our purposes it is sufficient.
 * Furthermore, this is the same implementation used by SLF4J for its logging formatting, which requires that it
 * be performant.
 *
 * <p> Note: If our formatting usages become constrained by SLF4J's implementation, then a custom implementation
 * could be warranted.
 */
public final class Format {

    /**
     * Generic formatting provided a format string and an argument array. Message parameters are delimited by
     * <code>"{}"</code> generically, or <code>"{0}"</code> with an argument index 0. For each argument, its
     * <code>toString()</code> implementation will be invoked during formatting.
     *
     * <p> For example: <code>format("one {} {}!", 2, "three")</code> outputs: <code>"one 2 three!"</code>.
     * <p> Another example: <code>format("one {1} {0}", "three", 2)</code> outputs the same as above.
     *
     * @param format Format string
     * @param args Arguments
     * @return Formatted string
     */
    public static String format(final String format, final Object ... args) {
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }

    /**
     * Follows the same formatting rules but includes information from the input {@link Throwable}, including its
     * stacktrace.
     *
     * @param format Format string
     * @param throwable Throwable
     * @param args Arguments
     * @return Formatted string with throwable details
     */
    public static String format(final String format, final Throwable throwable, final Object ... args) {
        return MessageFormatter.arrayFormat(format, args, throwable).getMessage();
    }
}

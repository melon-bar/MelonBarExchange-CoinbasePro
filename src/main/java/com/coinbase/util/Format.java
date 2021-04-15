package com.coinbase.util;

import org.slf4j.helpers.MessageFormatter;

public final class Format {

    public static String format(final String format, final Object ... args) {
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }

    public static String format(final String format, final Throwable throwable, final Object... args) {
        return MessageFormatter.arrayFormat(format, args, throwable).getMessage();
    }
}

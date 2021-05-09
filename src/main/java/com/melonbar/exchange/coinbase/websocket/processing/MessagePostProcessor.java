package com.melonbar.exchange.coinbase.websocket.processing;

import java.util.function.Predicate;

public interface MessagePostProcessor extends Predicate<String> {

    void process(final String message);

    default boolean test(final String message) {
        return true;
    }

    default boolean tryProcess(final String message) {
        if (test(message)) {
            process(message);
            return true;
        }
        return false;
    }
}

package com.melonbar.exchange.coinbase.websocket.processing.predicated;

import lombok.RequiredArgsConstructor;

import javax.websocket.MessageHandler;
import java.util.function.Predicate;

/**
 * Generalized wrapper for {@link MessageHandler.Whole} that guards {@link #onMessage} invocation using the
 * provided {@link Predicate}.
 *
 * @param <T> Message type
 */
@RequiredArgsConstructor
public class PredicatedMessageHandler<T> implements MessageHandler.Whole<T> {

    private final Predicate<T> predicate;
    private final MessageHandler.Whole<T> handler;

    /**
     * Guarded invocation of <code>handler#onMessage</code> by input {@link Predicate}.
     *
     * @param message Message
     */
    @Override
    public final void onMessage(final T message) {
        if (predicate.test(message)) {
            handler.onMessage(message);
        }
    }
}

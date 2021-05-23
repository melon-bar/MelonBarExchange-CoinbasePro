package com.melonbar.exchange.coinbase.websocket.processing;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.MessageHandler;

/**
 * Message handler that only accepts one message per set interval. All messages during the cooldown period will
 * be discarded. If the input {@link MessageHandler.Whole} is stateful, then it should not be listening to more than
 * one source of messages, unless it is thread-safe. If these constraints are not met, the delaying behavior will
 * become undefined.
 *
 * @param <T> Message type
 */
@Slf4j
public class TimedMessageHandler<T> implements MessageHandler.Whole<T> {

    private final MessageHandler.Whole<T> messageHandler;
    private final long delay;

    private long timestamp;

    /**
     * Simple constructor.
     *
     * @param messageHandler {@link MessageHandler.Whole}
     * @param delay Delay in milliseconds
     */
    public TimedMessageHandler(final MessageHandler.Whole<T> messageHandler, final long delay) {
        this.messageHandler = messageHandler;
        this.delay = delay;
    }

    /**
     * If the last invocation occurred more than <code>delay</code> milliseconds earlier, then
     * {@link MessageHandler.Whole#onMessage(T message)} is invoked. Otherwise, the message is discarded.
     *
     * @param message Message
     */
    @Override
    public void onMessage(final T message) {
        final long now = System.currentTimeMillis();
        if (now - timestamp > delay) {
            timestamp = now;
            messageHandler.onMessage(message);
        }
    }
}

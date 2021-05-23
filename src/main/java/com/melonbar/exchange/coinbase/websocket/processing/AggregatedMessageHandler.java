package com.melonbar.exchange.coinbase.websocket.processing;

import com.melonbar.exchange.coinbase.util.Guard;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.MessageHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles aggregations of {@link MessageHandler.Whole}. Only invoked on the retrieval of whole messages.
 * A single instance is thread-safe, and may be used across multiple threads for messaging handling.
 *
 * @param <T> Message type to be processed
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AggregatedMessageHandler<T> implements MessageHandler.Whole<T> {

    /**
     * Basic container for multiple message handlers. Uses {@link ArrayList} to take advantage of spacial locality
     * as this container will be iterated through frequently.
     */
    private final List<Whole<T>> messageHandlers = new ArrayList<>(10);

    /**
     * Creates an empty {@link AggregatedMessageHandler} instance.
     *
     * @param <T> Message type to be processed
     * @return {@link AggregatedMessageHandler}
     */
    public static <T> AggregatedMessageHandler<T> create() {
        return new AggregatedMessageHandler<>();
    }

    /**
     * Invokes all {@link MessageHandler.Whole#onMessage(T message)} aggregated by this instance in a thread-safe
     * manner. Invocation occurs following the same order they were added in.
     *
     * @param message Inbound message
     */
    @Override
    public void onMessage(final T message) {
        synchronized (messageHandlers) {
            for (final MessageHandler.Whole<T> handler : messageHandlers) {
                handler.onMessage(message);
            }
        }
    }

    /**
     * Registers a new {@link MessageHandler.Whole}. The input handler is added to the end of the list. The order by
     * which handlers are added is conserved by the holding data structure.
     *
     * @param messageHandler {@link MessageHandler.Whole} to be added
     */
    public void addMessageHandler(final MessageHandler.Whole<T> messageHandler) {
        Guard.nonNull(messageHandler);
        if (messageHandler instanceof AggregatedMessageHandler) {
            log.warn("Adding handler [{}] to itself will cause recursive message handler invocations!",
                    this.getClass().getName());
        }
        synchronized (messageHandlers) {
            messageHandlers.add(messageHandler);
        }
    }
}

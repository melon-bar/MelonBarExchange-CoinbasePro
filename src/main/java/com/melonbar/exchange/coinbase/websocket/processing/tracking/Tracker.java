package com.melonbar.exchange.coinbase.websocket.processing.tracking;

/**
 * Interface for tracking target value(s) based on inbound websocket feed messages. Each tracking instance should
 * be stateful, and have its state updated every time {@link #update(T message)} is invoked.
 *
 * @param <T> Message type
 */
public interface Tracker<T> {

    /**
     * Update current state using inbound message.
     *
     * @param message Inbound websocket message
     */
    void update(final T message);
}

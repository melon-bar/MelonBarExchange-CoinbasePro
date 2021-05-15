package com.melonbar.exchange.coinbase.websocket.message;

/**
 * Message to be sent by {@link com.melonbar.exchange.coinbase.websocket.WebsocketFeedClient}. Message being sent
 * is serialized using {@link #getText()}.
 */
public interface Message {

    /**
     * Serializes {@link Message}.
     *
     * @return Serialized message
     */
    String getText();
}

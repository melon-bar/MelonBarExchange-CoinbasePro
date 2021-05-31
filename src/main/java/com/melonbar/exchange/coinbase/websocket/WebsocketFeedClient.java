package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.websocket.message.Message;

import javax.websocket.CloseReason;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * Basic interface for a websocket feed TCP client.
 */
public interface WebsocketFeedClient {

    /**
     * Open connection with websocket provided an endpoint.
     *
     * @param endpoint Endpoint
     */
    void open(final String endpoint);

    /**
     * Close connection with websocket.
     */
    void close();

    /**
     * Performs defined action upon opening a new {@link Session}.
     *
     * @param session Newly-opened session
     */
    void onOpenConnection(final Session session);

    /**
     * Performs defined action upon the closing of input {@link Session}.
     *
     * @param session Closing session
     * @param closeReason Reason for closure
     */
    void onCloseConnection(final Session session, final CloseReason closeReason);

    /**
     * Sends input {@link Message} to target endpoint. Before being dispatched, the input {@link Message} will
     * have {@link Message#getText()} invoked for serialization.
     *
     * @param message Message to be sent
     */
    void sendMessage(final Message message);

    /**
     * Adds {@link MessageHandler.Whole} that handles {@link String} inbound messages.
     *
     * @param messageHandler {@link MessageHandler.Whole}
     */
    void addMessageHandler(final MessageHandler.Whole<String> messageHandler);
}

package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.websocket.message.Message;

import javax.websocket.CloseReason;
import javax.websocket.Session;

public interface WebsocketFeedClient {

    void onOpenConnection(final Session session);

    void onCloseConnection(final Session session, final CloseReason closeReason);

    void onMessageReceived(final String message);

    void sendMessage(final Message message);
}

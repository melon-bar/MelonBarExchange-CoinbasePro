package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.websocket.message.Message;
import com.melonbar.exchange.coinbase.websocket.processing.StringMessageHandler;

import javax.websocket.CloseReason;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public interface WebsocketFeedClient {

    void close();

    void onOpenConnection(final Session session);

    void onCloseConnection(final Session session, final CloseReason closeReason);

    void sendMessage(final Message message);
}

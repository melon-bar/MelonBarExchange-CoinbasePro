package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.websocket.processing.MessagePostProcessor;

public abstract class ReactiveWebsocketFeedClient implements WebsocketFeedClient {

    abstract void addMessagePostProcessor(final MessagePostProcessor messagePostProcessor);
}

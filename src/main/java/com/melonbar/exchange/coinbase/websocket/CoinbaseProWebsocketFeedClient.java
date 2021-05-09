package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.util.AppConfig;
import com.melonbar.exchange.coinbase.util.Guard;
import com.melonbar.exchange.coinbase.websocket.message.Message;
import com.melonbar.exchange.coinbase.websocket.processing.MessagePostProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CoinbaseProWebsocketFeedClient extends ReactiveWebsocketFeedClient {

    private final List<MessagePostProcessor> postProcessors = new LinkedList<>();
    private volatile boolean postProcessorsInitialized = false;

    private Session session;

    public CoinbaseProWebsocketFeedClient() {
        try {
            final WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            webSocketContainer.connectToServer(this,
                    URI.create(AppConfig.COINBASE_PRO_WEBHOOK_FEED_ENDPOINT));
        } catch (DeploymentException | IOException exception) {
            log.error("Got exception {} while initializing websocket container.",
                    exception.getClass().getName(), exception);
        }
    }

    @OnOpen
    @Override
    public void onOpenConnection(final Session session) {
        log.info("Opening new websocket feed session [{}]", session.getId());
        this.session = session;
    }

    @OnClose
    @Override
    public void onCloseConnection(final Session session, final CloseReason closeReason) {
        log.info("Closing current websocket feed session [{}], due to {}", session.getId(), closeReason);
        this.session = null;
    }

    @OnMessage
    @Override
    public void onMessageReceived(final String message) {
        if (postProcessorsInitialized) {
            react(message);
        }
    }

    @Override
    public void sendMessage(final Message message) {
        Guard.nonNull(message);
        this.session.getAsyncRemote().sendText(message.getText());
    }

    @Override
    public void addMessagePostProcessor(final MessagePostProcessor messagePostProcessor) {
        postProcessorsInitialized = true;
        synchronized (postProcessors) {
            postProcessors.add(messagePostProcessor);
        }
    }

    private void react(final String message) {
        synchronized (postProcessors) {
            for (final MessagePostProcessor messagePostProcessor : postProcessors) {
                messagePostProcessor.tryProcess(message);
            }
        }
    }
}

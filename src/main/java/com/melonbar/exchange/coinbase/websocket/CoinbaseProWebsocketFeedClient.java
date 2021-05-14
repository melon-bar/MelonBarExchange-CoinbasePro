package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.util.AppConfig;
import com.melonbar.exchange.coinbase.util.Guard;
import com.melonbar.exchange.coinbase.websocket.message.Message;
import com.melonbar.exchange.coinbase.websocket.processing.AggregatedMessageHandler;
import com.melonbar.exchange.coinbase.websocket.processing.StringMessageHandler;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

@Slf4j
@ClientEndpoint
public class CoinbaseProWebsocketFeedClient implements WebsocketFeedClient {

    private final AggregatedMessageHandler<String> aggregatedMessageHandler;
    private Session session;

    public CoinbaseProWebsocketFeedClient() {
        aggregatedMessageHandler = AggregatedMessageHandler.init();
        try {
            final WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            webSocketContainer.connectToServer(this,
                    URI.create(AppConfig.COINBASE_PRO_WEBHOOK_FEED_ENDPOINT));
        } catch (DeploymentException | IOException exception) {
            log.error("Got exception {} while initializing websocket container.",
                    exception.getClass().getName(), exception);
        }
    }

    public CoinbaseProWebsocketFeedClient withHandlers(final StringMessageHandler ... messageHandlers) {
        addMessageHandlers(messageHandlers);
        return this;
    }

    @OnOpen
    @Override
    public void onOpenConnection(final Session session) {
        log.info("Opening new websocket feed session [{}]", session.getId());
        this.session = session;
        this.session.addMessageHandler(String.class, aggregatedMessageHandler);
    }

    @OnClose
    @Override
    public void onCloseConnection(final Session session, final CloseReason closeReason) {
        log.info("Closing current websocket feed session [{}], due to {}", session.getId(), closeReason);
        this.session = null;
    }

    @Override
    public void close() {
        try {
            session.close(
                    new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,
                            "Closure by execution of close()"));
        } catch (IOException ioException) {
            log.warn("Could not close connection for session with ID: [{}]", session.getId());
        }
    }

    @Override
    public void sendMessage(final Message message) {
        Guard.nonNull(message);
        this.session.getAsyncRemote().sendText(message.getText());
    }

    public void addMessageHandlers(final StringMessageHandler ... messageHandlers) {
        for (final StringMessageHandler messageHandler : messageHandlers) {
            aggregatedMessageHandler.addMessageHandler(messageHandler);
        }
    }
}

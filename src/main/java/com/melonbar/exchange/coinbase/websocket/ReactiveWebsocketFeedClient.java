package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.websocket.message.Message;
import com.melonbar.exchange.coinbase.websocket.processing.AggregatedMessageHandler;
import com.melonbar.exchange.coinbase.websocket.processing.StringMessageHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;

/**
 * Abstract websocket feed client that "reacts" to inbound messages. Reactions defined by implementations of
 * {@link MessageHandler}, which are aggregated using {@link AggregatedMessageHandler}. This aggregation wrapper
 * is needed as each {@link Session websocket session} may only have one {@link MessageHandler} defined per
 * native websocket message type. Currently this implementation only accepts handling of {@link String} websocket
 * messages, enforced by usage of {@link StringMessageHandler}.
 *
 * <p> Extensions of this class are intended for synchronous access. However, implementations of {@link MessageHandler}
 * may be written asynchronously.
 */
@Slf4j
@Getter(AccessLevel.PROTECTED)
public abstract class ReactiveWebsocketFeedClient implements WebsocketFeedClient {

    private final AggregatedMessageHandler<String> aggregatedMessageHandler;
    private Session session;

    /**
     * Protected constructor that initializes the websocket feed connection using the provided endpoint.
     * Initializes {@link AggregatedMessageHandler}, which is registered once a {@link Session} is created
     * in {@link #onOpenConnection(Session)}.
     *
     * @param endpoint Websocket feed endpoint
     */
    protected ReactiveWebsocketFeedClient(final String endpoint) {
        aggregatedMessageHandler = AggregatedMessageHandler.create();
        open(endpoint);
    }

    /**
     * Overwrites stale/closed/null {@link Session} with the newly opened instance. Re-registers the member
     * {@link AggregatedMessageHandler}.
     *
     * @param session Newly-opened session
     */
    @OnOpen
    @Override
    public void onOpenConnection(final Session session) {
        log.info("Opening new websocket feed session [{}]", session.getId());
        this.session = session;
        this.session.addMessageHandler(String.class, aggregatedMessageHandler);
    }

    /**
     * Sets member {@link Session} to null and echos reason for session closure.
     *
     * @param session Closing session
     * @param closeReason Reason for closure
     */
    @OnClose
    @Override
    public void onCloseConnection(final Session session, final CloseReason closeReason) {
        log.info("Closing current websocket feed session [{}], due to {}", session.getId(), closeReason);
        this.session = null;
    }

    /**
     * Opens a new websocket feed session with the input TCP endpoint. By definition, once a successful connection
     * is established, {@link #onOpenConnection(Session)} is invoked with the newly-created {@link Session}.
     *
     * @param endpoint Endpoint
     */
    @Override
    public void open(final String endpoint) {
        try {
            ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, URI.create(endpoint));
        } catch (DeploymentException | IOException exception) {
            log.error("Got exception {} while initializing session to endpoint [{}].",
                    exception.getClass().getName(), endpoint, exception);
        }
    }

    /**
     * Closes the existing {@link Session} with closure reason {@link CloseReason.CloseCodes#NORMAL_CLOSURE}.
     */
    @Override
    public void close() {
        if (session == null) {
            log.warn("close() invoked during null session!");
            return;
        }
        try {
            session.close(
                    new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,
                            "Closure by execution of close()"));
        } catch (IOException ioException) {
            log.warn("Could not close connection for session with ID: [{}]", session.getId());
        }
    }

    /**
     * Sends {@link Message} using the async remote for {@link Session}.
     *
     * @param message Message to be sent
     */
    @Override
    public void sendMessage(final Message message) {
        this.session.getAsyncRemote().sendText(message.getText());
    }

    /**
     * Adds a {@link StringMessageHandler} to member instance of
     * {@link AggregatedMessageHandler aggregatedMessageHandler}.
     *
     * @param messageHandler Message handler to register
     */
    public void addMessageHandler(final StringMessageHandler messageHandler) {
        aggregatedMessageHandler.addMessageHandler(messageHandler);
    }
}

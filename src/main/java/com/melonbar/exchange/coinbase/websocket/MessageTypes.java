package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.websocket.message.ErrorMessage;
import com.melonbar.exchange.coinbase.websocket.message.FeedMessage;
import com.melonbar.exchange.coinbase.websocket.message.HeartbeatMessage;
import com.melonbar.exchange.coinbase.websocket.message.L2UpdateMessage;
import com.melonbar.exchange.coinbase.websocket.message.SnapshotMessage;
import com.melonbar.exchange.coinbase.websocket.message.SubscribeMessage;
import com.melonbar.exchange.coinbase.websocket.message.SubscriptionsMessage;
import com.melonbar.exchange.coinbase.websocket.message.TickerMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ActivatedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ChangedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ClosedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.MatchedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.OpenedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ReceivedOrderMessage;

/**
 * Contains definitions for all Coinbase Pro websocket feed message types.
 */
public final class MessageTypes {

    public static final String SUBSCRIBE = "subscribe";
    public static final String SUBSCRIPTIONS = "subscriptions";
    public static final String ERROR = "error";
    public static final String TICKER = "ticker";
    public static final String HEARTBEAT = "heartbeat";
    public static final String SNAPSHOT = "snapshot";
    public static final String L2_UPDATE = "l2update";

    // L3
    public static final String RECEIVED_ORDER = "received";
    public static final String OPENED_ORDER = "open";
    public static final String CLOSED_ORDER = "close";
    public static final String MATCHED_ORDER = "match";
    public static final String CHANGED_ORDER = "change";
    public static final String ACTIVATED_ORDER = "activate";

    /**
     * Evaluates the {@link Class} for the corresponding {@link FeedMessage} subtype based on the <code>type</code>
     * field from an inbound message. Used for dynamic type detection for unmarshalling.
     *
     * @param type Type field
     * @return Corresponding extension of {@link FeedMessage}
     * @throws IllegalStateException Provided a <code>type</code> that does not map to any {@link FeedMessage}.
     */
    public static Class<? extends FeedMessage> evaluateMessageType(final String type) {
        return switch (type) {
            case SUBSCRIBE -> SubscribeMessage.class;
            case SUBSCRIPTIONS -> SubscriptionsMessage.class;
            case ERROR -> ErrorMessage.class;
            case TICKER -> TickerMessage.class;
            case HEARTBEAT -> HeartbeatMessage.class;
            case SNAPSHOT -> SnapshotMessage.class;
            case L2_UPDATE -> L2UpdateMessage.class;
            case RECEIVED_ORDER -> ReceivedOrderMessage.class;
            case OPENED_ORDER -> OpenedOrderMessage.class;
            case CLOSED_ORDER -> ClosedOrderMessage.class;
            case MATCHED_ORDER -> MatchedOrderMessage.class;
            case CHANGED_ORDER -> ChangedOrderMessage.class;
            case ACTIVATED_ORDER -> ActivatedOrderMessage.class;
            default -> throw new IllegalStateException("Unexpected type: " + type);
        };
    }
}

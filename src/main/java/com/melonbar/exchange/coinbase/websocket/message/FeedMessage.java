package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.websocket.MessageTypes;
import com.melonbar.exchange.coinbase.websocket.message.deserializer.JsonMessageMapper;
import com.melonbar.exchange.coinbase.websocket.message.full.ActivatedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ChangedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ClosedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.MatchedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.OpenedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ReceivedOrderMessage;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * Parent POJO abstract class for json messages to be received and sent via websocket feed. Intended for the
 * Coinbase Pro websocket feed.
 *
 * <p> All subtypes when json serialized will automatically have field <code>type</code>, which is determined by
 * field <code>name</code> in the corresponding {@link JsonSubTypes.Type} annotation. All null fields are omitted
 * when marshalled.
 *
 * @see Message For base definition
 * @see JsonMessageMapper For json marshalling/unmarshalling logic
 */
@Slf4j
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = FeedMessage.TYPE_FIELD)
@JsonSubTypes({
        // request message types
        @JsonSubTypes.Type(value = SubscribeMessage.class,      name = MessageTypes.SUBSCRIBE),

        // response message types
        @JsonSubTypes.Type(value = SubscriptionsMessage.class,  name = MessageTypes.SUBSCRIPTIONS),
        @JsonSubTypes.Type(value = ErrorMessage.class,          name = MessageTypes.ERROR),
        @JsonSubTypes.Type(value = TickerMessage.class,         name = MessageTypes.TICKER),
        @JsonSubTypes.Type(value = HeartbeatMessage.class,      name = MessageTypes.HEARTBEAT),
        @JsonSubTypes.Type(value = SnapshotMessage.class,       name = MessageTypes.SNAPSHOT),
        @JsonSubTypes.Type(value = L2UpdateMessage.class,       name = MessageTypes.L2_UPDATE),

        // full (L3) message types
        @JsonSubTypes.Type(value = ReceivedOrderMessage.class,  name = MessageTypes.RECEIVED_ORDER),
        @JsonSubTypes.Type(value = OpenedOrderMessage.class,    name = MessageTypes.OPENED_ORDER),
        @JsonSubTypes.Type(value = ClosedOrderMessage.class,    name = MessageTypes.CLOSED_ORDER),
        @JsonSubTypes.Type(value = MatchedOrderMessage.class,   name = MessageTypes.MATCHED_ORDER),
        @JsonSubTypes.Type(value = ChangedOrderMessage.class,   name = MessageTypes.CHANGED_ORDER),
        @JsonSubTypes.Type(value = ActivatedOrderMessage.class, name = MessageTypes.ACTIVATED_ORDER),
})
public abstract class FeedMessage implements Message {

    public static final String TYPE_FIELD = "type";

    @JsonProperty("sequence") private Long sequence;
    @JsonProperty("time") private DateTime time;
    @JsonProperty("product_id") private ProductId productId;

    /**
     * Marshals instance using {@link JsonMessageMapper}.
     *
     * @return Json string version of instance
     */
    @JsonIgnore
    @Override
    public String getText() {
        return JsonMessageMapper.objectToJson(this);
    }

    /**
     * Override <code>toString</code> to return json representation.
     *
     * @return Json representation
     */
    @Override
    public String toString() {
        return getText();
    }

    /**
     * Equality operator based on type check and json message content.
     *
     * @param other Other object
     * @return True if input is a {@link FeedMessage} and has the same deserialized output
     */
    @Override
    public boolean equals(final Object other) {
        return (other instanceof FeedMessage jsonFeedMessage) && getText().equals(jsonFeedMessage.getText());
    }
}

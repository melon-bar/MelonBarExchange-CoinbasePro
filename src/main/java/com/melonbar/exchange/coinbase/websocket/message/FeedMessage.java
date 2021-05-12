package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

@Slf4j
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
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

    @JsonProperty("sequence") private Long sequence;
    @JsonProperty("time") private DateTime time;
    @JsonProperty("product_id") private ProductId productId;

    @JsonIgnore
    @Override
    public String getText() {
        return JsonMessageMapper.objectToJson(this);
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public boolean equals(final Object other) {
        return (other instanceof FeedMessage jsonFeedMessage) && getText().equals(jsonFeedMessage.getText());
    }
}

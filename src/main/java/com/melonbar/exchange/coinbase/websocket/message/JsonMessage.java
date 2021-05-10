package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.websocket.message.deserializer.JsonMessageMapper;
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
        @JsonSubTypes.Type(value = SubscribeMessage.class, name = "subscribe"),

        // response message types
        @JsonSubTypes.Type(value = SubscriptionsMessage.class, name = "subscriptions"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "error"),
        @JsonSubTypes.Type(value = TickerMessage.class, name = "ticker"),
        @JsonSubTypes.Type(value = HeartbeatMessage.class, name = "heartbeat"),
        @JsonSubTypes.Type(value = SnapshotMessage.class, name = "snapshot"),
        @JsonSubTypes.Type(value = L2UpdateMessage.class, name = "l2update"),
})
public abstract class JsonMessage implements Message {

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
        return (other instanceof JsonMessage jsonMessage) && getText().equals(jsonMessage.getText());
    }
}

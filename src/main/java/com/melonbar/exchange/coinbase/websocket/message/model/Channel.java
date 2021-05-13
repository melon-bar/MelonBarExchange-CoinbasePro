package com.melonbar.exchange.coinbase.websocket.message.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Channel {

    @JsonProperty("name") private ChannelType name;
    @JsonProperty("product_ids") private ProductId[] productIds;

    public enum ChannelType {
        HEARTBEAT, STATUS, TICKER, LEVEL2, USER, MATCHES, FULL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        @JsonValue
        public String toJson() {
            return toString();
        }
    }
}

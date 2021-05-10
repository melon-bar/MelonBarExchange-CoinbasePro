package com.melonbar.exchange.coinbase.websocket.message.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonValue;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Channel {

    @JsonProperty("name") final ChannelType name;
    @JsonProperty("product_ids") final ProductId[] productIds;

    public static Channel[] of(final Channel ... channels){
        return channels;
    }

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

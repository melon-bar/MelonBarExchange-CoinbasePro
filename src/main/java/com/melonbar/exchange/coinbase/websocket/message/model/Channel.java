package com.melonbar.exchange.coinbase.websocket.message.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public static Channel of(final ChannelType channelType, final ProductId ... productIds) {
        return new Channel(channelType, productIds);
    }
}

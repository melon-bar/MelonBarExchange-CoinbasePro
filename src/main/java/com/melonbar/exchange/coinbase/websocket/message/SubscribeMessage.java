package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.core.model.ProductId;
import com.melonbar.exchange.coinbase.websocket.message.model.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeMessage extends FeedMessage {

    @JsonProperty("product_ids")
    @Builder.Default
    private ProductId[] productIds = new ProductId[] {};

    @JsonProperty("channels")
    @Builder.Default
    private Channel[] channels = new Channel[] {};
}

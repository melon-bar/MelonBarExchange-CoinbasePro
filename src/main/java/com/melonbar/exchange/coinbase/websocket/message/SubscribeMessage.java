package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.websocket.message.model.Channel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeMessage extends FeedMessage {

    @JsonProperty("product_ids") private ProductId[] productIds = new ProductId[] {};
    @JsonProperty("channels") private Channel[] channels = new Channel[] {};
}

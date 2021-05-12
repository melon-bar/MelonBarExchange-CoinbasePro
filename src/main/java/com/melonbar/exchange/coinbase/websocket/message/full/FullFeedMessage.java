package com.melonbar.exchange.coinbase.websocket.message.full;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import com.melonbar.exchange.coinbase.websocket.message.FeedMessage;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
public abstract class FullFeedMessage extends FeedMessage {

    @JsonProperty("side") private OrderSide orderSide;
    @JsonProperty("order_id") private String orderId;
    @JsonProperty("size") private BigDecimal orderSize;
    @JsonProperty("price") private BigDecimal price;
    @JsonProperty("remaining_size") private BigDecimal remainingOrderSize;
}
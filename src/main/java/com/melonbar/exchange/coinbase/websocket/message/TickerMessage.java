package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TickerMessage extends JsonMessage {

    @JsonProperty("trade_id") private Long tradeId;
    @JsonProperty("price") private BigDecimal price;
    @JsonProperty("side") private OrderSide side;
    @JsonProperty("last_size") private BigDecimal lastSize;
    @JsonProperty("best_bid") private BigDecimal bestBid;
    @JsonProperty("best_ask") private BigDecimal bestAsk;
}

package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerMessage extends JsonMessage {

    @JsonProperty("trade_id") private Long tradeId;
    @JsonProperty("sequence") private Long sequence;
    @JsonProperty("time") private DateTime time;
    @JsonProperty("product_id") private ProductId productId;
    @JsonProperty("price") private BigDecimal price;
    @JsonProperty("side") private OrderSide side;
    @JsonProperty("last_size") private BigDecimal lastSize;
    @JsonProperty("best_bid") private BigDecimal bestBid;
    @JsonProperty("best_ask") private BigDecimal bestAsk;
}

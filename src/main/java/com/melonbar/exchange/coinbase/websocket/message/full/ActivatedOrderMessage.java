package com.melonbar.exchange.coinbase.websocket.message.full;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.exchange.coinbase.model.order.flag.OrderStop;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public class ActivatedOrderMessage extends FullFeedMessage {

    @JsonProperty("timestamp") private BigDecimal timestamp; // TODO: check if compatible with joda time
    @JsonProperty("user_id") private String userId;
    @JsonProperty("stop_type") private OrderStop stopType;
    @JsonProperty("stop_price") private BigDecimal stopPrice;
    @JsonProperty("funds") private BigDecimal funds;
    @JsonProperty("private") private Boolean isPrivate;
}

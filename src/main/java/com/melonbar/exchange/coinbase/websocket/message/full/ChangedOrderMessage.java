package com.melonbar.exchange.coinbase.websocket.message.full;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public class ChangedOrderMessage extends FullFeedMessage {

    @JsonProperty("new_size") private BigDecimal newOrderSize;
    @JsonProperty("old_size") private BigDecimal oldOrderSize;
}

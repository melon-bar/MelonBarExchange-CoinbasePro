package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class HeartbeatMessage extends FeedMessage {

    @JsonProperty("last_trade_id") private Long lastTradeId;
}

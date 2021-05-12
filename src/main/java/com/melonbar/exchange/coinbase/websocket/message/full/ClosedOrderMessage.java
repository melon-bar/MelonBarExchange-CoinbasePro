package com.melonbar.exchange.coinbase.websocket.message.full;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ClosedOrderMessage extends FullFeedMessage {

    @JsonProperty("reason") private String reason;
}

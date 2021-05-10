package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.exchange.coinbase.websocket.message.model.Channel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionsMessage extends JsonMessage {

    @JsonProperty("channels") private Channel[] channels;
}

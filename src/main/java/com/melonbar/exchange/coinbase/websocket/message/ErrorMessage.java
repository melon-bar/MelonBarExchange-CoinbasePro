package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage extends JsonMessage {

    @JsonProperty("message") private String message;
}

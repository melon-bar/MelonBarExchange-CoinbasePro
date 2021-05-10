package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.exchange.coinbase.websocket.message.model.L2OrderTuple;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotMessage extends JsonMessage {

    @JsonProperty("bids") private L2OrderTuple[] bids;
    @JsonProperty("asks") private L2OrderTuple[] asks;
}

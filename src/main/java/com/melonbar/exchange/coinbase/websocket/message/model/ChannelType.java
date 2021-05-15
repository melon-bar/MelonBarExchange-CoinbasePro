package com.melonbar.exchange.coinbase.websocket.message.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChannelType {

    HEARTBEAT, STATUS, TICKER, LEVEL2, USER, MATCHES, FULL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonValue
    public String toJson() {
        return toString();
    }
}
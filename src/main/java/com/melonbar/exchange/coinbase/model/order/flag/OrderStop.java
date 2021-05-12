package com.melonbar.exchange.coinbase.model.order.flag;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStop {

    LOSS, ENTRY;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}

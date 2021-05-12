package com.melonbar.exchange.coinbase.model.order.flag;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderSide {

    BUY, SELL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonValue
    public String toJson() {
        return toString();
    }
}

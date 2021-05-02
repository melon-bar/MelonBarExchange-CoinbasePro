package com.coinbase.exchange.model.order.flag;

public enum OrderType {
    LIMIT, MARKET;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

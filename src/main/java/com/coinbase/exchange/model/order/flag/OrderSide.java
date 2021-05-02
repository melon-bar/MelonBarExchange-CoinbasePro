package com.coinbase.exchange.model.order.flag;

public enum OrderSide {
    BUY, SELL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

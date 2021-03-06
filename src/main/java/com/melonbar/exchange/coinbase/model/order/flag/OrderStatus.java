package com.melonbar.exchange.coinbase.model.order.flag;

public enum OrderStatus {

    OPEN, PENDING, ACTIVE, DONE, SETTLED, ALL;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

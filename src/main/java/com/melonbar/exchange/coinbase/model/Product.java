package com.melonbar.exchange.coinbase.model;

public record Product(Currency.Unit left, Currency.Unit right) {

    @Override
    public String toString() {
        return left.name() + "-" + right.name();
    }
}
package com.coinbase.exchange.model;

public record Product(Currency.Unit left, Currency.Unit right) {

    @Override
    public String toString() {
        return left.name() + "-" + right.name();
    }
}

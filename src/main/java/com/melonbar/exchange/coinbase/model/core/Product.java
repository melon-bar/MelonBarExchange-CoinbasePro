package com.melonbar.exchange.coinbase.model.core;

public record Product(Currency.Unit left, Currency.Unit right) {

    public static Product of(final String left, final String right) {
        return new Product(
                Currency.Unit.valueOf(left.toUpperCase()),
                Currency.Unit.valueOf(right.toUpperCase()));
    }

    @Override
    public String toString() {
        return left.name() + "-" + right.name();
    }
}

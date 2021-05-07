package com.melonbar.exchange.coinbase.model.core;

/**
 * Represents a product of Coinbase Pro, or in other words, the available currency pairs
 * for trading. Also referred to as "base currency" and "quote currency".
 *
 * @param left Base currency
 * @param right Quote currency
 */
public record Product(Currency.Unit left, Currency.Unit right) {

    public static final Product ETH_USD = new Product(Currency.Unit.ETH, Currency.Unit.USD);

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

package com.melonbar.exchange.coinbase.model.core;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents a product of Coinbase Pro, or in other words, the available currency pairs
 * for trading. Also referred to as "base currency" and "quote currency".
 *
 * @param left Base currency
 * @param right Quote currency
 */
public record ProductId(Currency.Unit left, Currency.Unit right) {

    public static final String DELIMITER = "-";
    public static final ProductId ETH_USD = new ProductId(Currency.Unit.ETH, Currency.Unit.USD);

    public static ProductId of(final String left, final String right) {
        return new ProductId(
                Currency.Unit.valueOf(left.toUpperCase()),
                Currency.Unit.valueOf(right.toUpperCase()));
    }

    @Override
    public String toString() {
        return left.name() + DELIMITER + right.name();
    }

    @JsonValue
    public String toJson() {
        return toString();
    }
}

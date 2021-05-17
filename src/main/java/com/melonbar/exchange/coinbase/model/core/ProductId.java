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
    public static final ProductId BTC_USD = new ProductId(Currency.Unit.BTC, Currency.Unit.USD);

    public static ProductId of(final String left, final String right) {
        return new ProductId(
                Currency.Unit.unitOf(left),
                Currency.Unit.unitOf(right));
    }

    @Override
    public String toString() {
        return left.getSymbol() + DELIMITER + right.getSymbol();
    }

    @JsonValue
    public String toJson() {
        return toString();
    }
}

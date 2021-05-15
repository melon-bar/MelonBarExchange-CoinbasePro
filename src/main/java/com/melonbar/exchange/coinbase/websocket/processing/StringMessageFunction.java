package com.melonbar.exchange.coinbase.websocket.processing;

import java.util.function.Function;

/**
 * Generic interface extension of {@link Function} for accepting {@link String} types.
 *
 * @param <T> Resultant type
 */
public interface StringMessageFunction<T> extends Function<String, T> {

}

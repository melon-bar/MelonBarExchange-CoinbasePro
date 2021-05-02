package com.coinbase.exchange.model.response;

import java.util.function.Function;

/**
 * Functional abstraction for all {@link Function}s that accept {@link Response} as input, and provides some
 * arbitrary output of type {@link T}.
 *
 * @param <T> Resultant type of functional evaluation
 */
public interface PostProcessor<T> extends Function<Response, T> {

}

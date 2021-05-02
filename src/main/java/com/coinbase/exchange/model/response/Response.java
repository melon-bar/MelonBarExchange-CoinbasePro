package com.coinbase.exchange.model.response;

import com.coinbase.exchange.util.Format;

import java.net.http.HttpHeaders;
import java.util.function.Function;

/**
 * HTTP response from a request dispatched by an implementation of {@link com.coinbase.exchange.http.HttpClient}.
 * Serves as a rudimentary container for all response info, namely the response body (string in json format),
 * headers, and the status code.
 */
public record Response(String content, HttpHeaders headers, int statusCode) {

    /**
     * Represents an empty or non-response.
     */
    public static final Response EMPTY_RESPONSE = new Response("", null, -1);

    /**
     * @return True if response is equivalent to statically defined empty response, false otherwise.
     */
    public boolean isEmpty() {
        return equals(EMPTY_RESPONSE);
    }

    /**
     * Applies input {@link PostProcessor} on <code>this</code> as a parameter, and returns the result.
     *
     * @param postProcessor {@link PostProcessor}, a function accepting input {@link Response} and outputting {@link T}
     * @param <T> Target output type of <code>postProcessor</code>
     * @return Result of function <code>postProcessor</code>, an instance of {@link T}
     */
    public <T> T process(final PostProcessor<T> postProcessor) {
        return postProcessor.apply(this);
    }

    /**
     * Technically is no different than the <code>process()</code> that accepts {@link PostProcessor}, but provides more
     * flexibility. For example, this extension allows the application of <code>andThen</code> and <code>compose</code>
     * chained after an input {@link PostProcessor}.
     *
     * @param function {@link Function} that accepts input {@link Response} and outputs {@link T}
     * @param <T> Target output type
     * @return Result of function <code>function</code>
     */
    public <T> T process(final Function<Response, T> function) {
        return function.apply(this);
    }

    /**
     * Rudimentary string representation of {@link Response}, namely for debugging/trace purposes.
     *
     * @return String representation of {@link Response}
     */
    @Override
    public String toString() {
        return Format.format("Response(status=[{}], content=[{}], headers=[{}])", statusCode, content, headers);
    }

    /**
     * Evaluates equality based on parameter (assuming it is an instance of {@link Response}) content and status code.
     * Note that this equality check is very finicky, as HTTP responses contain very granular field values, such as
     * timestamps. In other words, most HTTP responses will not be "equal" under this definition of equality.
     *
     * <p> A cleaner and more reliable approach would be to define/utilize the <code>equals</code> definition based on
     * the resultant type of post-processing.
     *
     * @param object Other
     * @return True if input is equal to <code>this</code>, false otherwise.
     */
    @Override
    public boolean equals(final Object object) {
        return object instanceof Response response
                && content.equals(response.content())
                && statusCode == response.statusCode();
    }
}

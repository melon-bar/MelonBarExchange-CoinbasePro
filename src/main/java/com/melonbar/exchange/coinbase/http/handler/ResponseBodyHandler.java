package com.melonbar.exchange.coinbase.http.handler;

import com.melonbar.exchange.coinbase.model.response.Response;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Body handler for mapping the {@link HttpResponse} content to a {@link Response} instance.
 */
public class ResponseBodyHandler implements HttpResponse.BodyHandler<Response> {

    /**
     * Applies basic mapping to {@link Response} using the result {@link HttpResponse.ResponseInfo}. Stores the
     * string body content, the response header, and the response status code.
     *
     * @param responseInfo the response info
     * @return a body subscriber
     */
    @Override
    public HttpResponse.BodySubscriber<Response> apply(final HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                body -> new Response(body, responseInfo.headers(), responseInfo.statusCode()));
    }
}

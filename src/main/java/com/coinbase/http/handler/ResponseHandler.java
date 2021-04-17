package com.coinbase.http.handler;

import com.coinbase.model.Response;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ResponseHandler implements HttpResponse.BodyHandler<Response> {


    /**
     *
     *
     * @param responseInfo the response info
     * @return a body subscriber
     */
    @Override
    public HttpResponse.BodySubscriber<Response> apply(final HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                body -> new Response(body, responseInfo.headers(), responseInfo.statusCode())
        );
    }
}

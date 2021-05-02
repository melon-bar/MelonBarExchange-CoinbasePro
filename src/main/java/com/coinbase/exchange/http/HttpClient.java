package com.coinbase.exchange.http;

import com.coinbase.exchange.model.response.Response;
import com.coinbase.exchange.model.request.BaseRequest;

import java.net.http.HttpResponse;

/**
 * Generic HTTP client interface that accepts {@link BaseRequest} types.
 */
public interface HttpClient {

    /**
     * Accepts {@link BaseRequest} and performs some HTTP request, whose result is returned as
     * {@link HttpResponse<Response>}.
     *
     * @param request Request
     * @return {@link HttpResponse<Response>}
     */
    HttpResponse<Response> send(final BaseRequest request);
}

package com.melonbar.exchange.coinbase.http;

import com.melonbar.exchange.coinbase.model.response.Response;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;

import java.net.http.HttpResponse;
import java.util.concurrent.Future;

/**
 * Generic HTTP client interface that accepts {@link BaseRequest} types.
 */
public interface HttpClient {

    /**
     * Accepts {@link BaseRequest} and performs some HTTP request, whose result is returned as
     * {@link HttpResponse< Response >}.
     *
     * @param request Request
     * @return {@link HttpResponse<Response>}
     */
    HttpResponse<Response> send(final BaseRequest request);

    /**
     * Accepts {@link BaseRequest} and performs some HTTP request asynchronously, whose result is returned as
     * a {@link Future} containing the {@link HttpResponse<Response>}.
     *
     * @param request Request
     * @return {@link Future}
     */
    Future<HttpResponse<Response>> sendAsync(final BaseRequest request);
}

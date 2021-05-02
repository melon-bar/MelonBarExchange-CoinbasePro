package com.coinbase.exchange.http;

import com.coinbase.exchange.authentication.Authentication;
import com.coinbase.exchange.http.handler.ResponseBodyHandler;
import com.coinbase.exchange.model.response.Response;
import com.coinbase.exchange.model.request.BaseRequest;
import com.coinbase.exchange.util.Format;
import com.coinbase.exchange.util.request.Requests;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * HTTP client record implementation, which uses {@link java.net.http.HttpClient} as the underlying driver for
 * dispatching requests the provided endpoint. This implementation expects all request information to be provided
 * by the input {@link BaseRequest}. This client determines all HTTP request parameters dynamically.
 *
 * <p> This implementation's intent is to make authenticated requests, requiring an implementation of
 * {@link Authentication} to be provided.
 *
 * <p> TODO: asynchronous request dispatch.
 */
@Slf4j
public record HttpClientImpl(Authentication authentication,
                             java.net.http.HttpClient httpClient) implements HttpClient {

    /**
     * Dispatches an {@link HttpRequest} <i>synchronously</i>, generated from {@link BaseRequest} using
     * {@link java.net.http.HttpClient}. Handles response body using {@link ResponseBodyHandler}. The response body,
     * {@link Response}, contains the result headers, status code, and string content.
     *
     * <p> The caller of this dispatch has authority of any post-processing or object mapping on the result.
     *
     * @param request Extension of {@link BaseRequest}
     * @return {@link HttpResponse<Response>}
     */
    @Override
    public HttpResponse<Response> send(final BaseRequest request) {
        try {
            log.info("Dispatching request [{}] with method [{}] to URL: [{}]",
                    request.getClass().getSimpleName(), request.getMethod(), request.getUri());
            return httpClient.send(generateHttpRequest(request), new ResponseBodyHandler());
        } catch (Exception exception) {
            log.error(Format.format("Something went wrong while dispatching request: [{}]",
                    Requests.toString(request)), exception);
        }
        return null;
    }

    /**
     * Converts the {@link BaseRequest} into the {@link HttpRequest} for dispatch, and handles any required signing
     * using {@link Authentication}.
     *
     * @param request {@link BaseRequest}
     * @return {@link HttpRequest} ready for dispatch
     */
    private HttpRequest generateHttpRequest(final BaseRequest request) {
        return authentication.sign(
                    HttpRequest.newBuilder(),
                    request.getMethod().name(),
                    request.getRequestPath(),
                    request.getBody())
                // apply URI
                .uri(URI.create(request.getUri()))
                // apply HTTP method and body
                .method(
                    request.getMethod().name(),
                    HttpRequest.BodyPublishers.ofString(request.getBody()))
                .build();
    }
}

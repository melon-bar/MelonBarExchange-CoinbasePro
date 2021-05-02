package com.melonbar.exchange.coinbase.authentication;

import com.melonbar.exchange.coinbase.http.Http;

import java.net.http.HttpRequest;

/**
 * Generic authentication interface intended to be applied to HTTP requests before dispatch.
 */
public interface Authentication {

    /**
     * Applies authentication signature on the HTTP request being built, as a function of the intended method,
     * URI, and request body.
     *
     * @param httpRequestBuilder HTTP request being built
     * @param method HTTP method, probably {@link Http} as a string
     * @param uri URI
     * @param body Request body as string, probably in JSON format
     * @return Signed {@link HttpRequest.Builder}
     */
    HttpRequest.Builder sign(final HttpRequest.Builder httpRequestBuilder,
                             final String method,
                             final String uri,
                             final String body);
}

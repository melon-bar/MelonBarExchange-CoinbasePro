package com.melonbar.exchange.coinbase.authentication;

import java.net.http.HttpRequest;

public class NoAuthentication implements Authentication {

    @Override
    public HttpRequest.Builder sign(final HttpRequest.Builder httpRequestBuilder,
                                    final String method,
                                    final String requestPath,
                                    final String body) {
        return httpRequestBuilder;
    }
}

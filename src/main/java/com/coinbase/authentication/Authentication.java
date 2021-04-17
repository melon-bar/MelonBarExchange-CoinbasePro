package com.coinbase.authentication;

import java.net.http.HttpRequest;

public interface Authentication {

    HttpRequest.Builder sign(final HttpRequest.Builder httpRequestBuilder,
                             final String method,
                             final String uriPath,
                             final String body);
}

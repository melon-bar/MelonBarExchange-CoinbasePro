package com.coinbase.client.http;

import com.coinbase.model.Response;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface HttpClient {

    HttpResponse<Response> get(final HttpRequest httpRequest);

    HttpResponse<Response> put(final HttpRequest httpRequest);

    HttpResponse<Response> post(final HttpRequest httpRequest);

    HttpResponse<Response> delete(final HttpRequest httpRequest);
}

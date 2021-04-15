package com.coinbase.util.router;

import com.coinbase.client.http.Http;
import com.coinbase.client.http.HttpClient;
import com.coinbase.model.Response;
import lombok.NonNull;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class Router {

    public static HttpResponse<Response> routeRequest(@NonNull final HttpClient httpClient, @NonNull final HttpRequest httpRequest) {
        return switch (Http.valueOf(httpRequest.method())) {
            case GET -> httpClient.get(httpRequest);
            case PUT -> httpClient.put(httpRequest);
            case POST -> httpClient.post(httpRequest);
            case DELETE -> httpClient.delete(httpRequest);
        };
    }
}

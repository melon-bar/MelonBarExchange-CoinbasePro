package com.coinbase.http;

import com.coinbase.authentication.Authentication;
import com.coinbase.http.handler.ResponseHandler;
import com.coinbase.model.Response;
import com.coinbase.model.request.BaseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.UriBuilder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
public record HttpClientImpl(Authentication authentication,
                             java.net.http.HttpClient httpClient) implements HttpClient {

    @Override
    public HttpResponse<Response> send(final BaseRequest request) {
        try {
            return httpClient.send(generateHttpRequest(request), new ResponseHandler());
        } catch (Exception __) {
            // TODO
        }
        return null;
    }

    private HttpRequest generateHttpRequest(final BaseRequest request) {
        final HttpRequest.Builder httpRequestContent = authentication.sign(
                HttpRequest.newBuilder(),
                request.getMethod().name(),
                request.getUri(),
                request.getBody())
                .uri(UriBuilder.fromUri(request.getUri()).build());
        return applyMethod(httpRequestContent, request.getMethod()).build();
    }

    private HttpRequest.Builder applyMethod(final HttpRequest.Builder builder, final Http method) {
        return switch (method) {
            case GET -> builder.GET();
            case DELETE -> builder.DELETE();
            default -> builder.method(method.name(), HttpRequest.BodyPublishers.noBody());
        };
    }
}

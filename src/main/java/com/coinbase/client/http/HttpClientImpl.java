package com.coinbase.client.http;

import com.coinbase.client.http.handler.ResponseHandler;
import com.coinbase.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
public class HttpClientImpl implements HttpClient {

    final java.net.http.HttpClient httpClient;

    // https://stackoverflow.com/questions/57629401/deserializing-json-using-java-11-httpclient-and-custom-bodyhandler-with-jackson
    @Override
    public HttpResponse<Response> get(final HttpRequest httpRequest) {
        try {
            HttpResponse<Response> stringHttpResponse = httpClient.send(httpRequest, new ResponseHandler());
        } catch (IOException ioException) {

        } catch (InterruptedException interruptedException) {

        }
        return null;
    }

    @Override
    public HttpResponse<Response> put(final HttpRequest httpRequest) {
        return null;
    }

    @Override
    public HttpResponse<Response> post(final HttpRequest httpRequest) {
        return null;
    }

    @Override
    public HttpResponse<Response> delete(HttpRequest httpRequest) {
        return null;
    }
}

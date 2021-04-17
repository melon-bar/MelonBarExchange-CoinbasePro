package com.coinbase.http;

import com.coinbase.model.Response;
import com.coinbase.model.request.BaseRequest;

import java.net.http.HttpResponse;

public interface HttpClient {

    HttpResponse<Response> send(final BaseRequest request);
}

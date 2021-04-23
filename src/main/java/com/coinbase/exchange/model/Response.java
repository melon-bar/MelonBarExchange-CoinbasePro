package com.coinbase.exchange.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpHeaders;


@Getter
@RequiredArgsConstructor
public class Response {

    private final String content;
    private final HttpHeaders headers;
    private final int statusCode;
}

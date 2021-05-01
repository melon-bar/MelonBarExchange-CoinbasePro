package com.coinbase.exchange.model;

import com.coinbase.exchange.util.Format;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpHeaders;


@Getter
@RequiredArgsConstructor
public class Response {

    private final String content;
    private final HttpHeaders headers;
    private final int statusCode;

    @Override
    public String toString() {
        return Format.format("Response(status=[{}], content=[{}], headers=[{}])", statusCode, content, headers);
    }
}

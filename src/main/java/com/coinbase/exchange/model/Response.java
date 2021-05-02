package com.coinbase.exchange.model;

import com.coinbase.exchange.util.Format;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpHeaders;


@Getter
@RequiredArgsConstructor
public record Response(String content, HttpHeaders headers, int statusCode) {

    public static Response empty() {
        return new Response("", null, -1);
    }

    @Override
    public String toString() {
        return Format.format("Response(status=[{}], content=[{}], headers=[{}])", statusCode, content, headers);
    }
}

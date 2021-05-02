package com.melonbar.exchange.coinbase.util.request;

import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import com.melonbar.exchange.coinbase.util.Format;

public final class Requests {

    public static String toString(final BaseRequest request) {
        return request == null ? "null" : Format.format("{}(uri={}, method={}, body={})",
                request.getClass().getSimpleName(),
                request.getUri(),
                request.getMethod(),
                request.getBody());
    }
}

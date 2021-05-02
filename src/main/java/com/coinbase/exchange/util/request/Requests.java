package com.coinbase.exchange.util.request;

import com.coinbase.exchange.model.request.BaseRequest;
import com.coinbase.exchange.util.Format;

public final class Requests {

    public static String toString(final BaseRequest request) {
        return request == null ? "null" : Format.format("{}(uri={}, method={}, body={})",
                request.getClass().getSimpleName(),
                request.getUri(),
                request.getMethod(),
                request.getBody());
    }
}

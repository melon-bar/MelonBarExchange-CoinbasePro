package com.coinbase.exchange.util;

import com.coinbase.exchange.model.request.BaseRequest;

public final class RequestUtils {

    public static String toString(final BaseRequest request) {
        return request == null ? "null" : Format.format("{}(uri={}, method={}, body={})",
                request.getClass().getSimpleName(),
                request.getUri(),
                request.getMethod(),
                request.getBody());
    }
}

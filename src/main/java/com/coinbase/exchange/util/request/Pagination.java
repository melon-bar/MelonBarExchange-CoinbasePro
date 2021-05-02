package com.coinbase.exchange.util.request;

import com.coinbase.exchange.util.Guard;

public record Pagination(Cursor cursor, int page, int limit) {

    public enum Cursor {
        BEFORE,
        AFTER;
    }

    public String toString() {
        Guard.nonNull(cursor);
        return "?" + cursor.name().toLowerCase() + "=" + page + "&limit=" + limit;
    }
}

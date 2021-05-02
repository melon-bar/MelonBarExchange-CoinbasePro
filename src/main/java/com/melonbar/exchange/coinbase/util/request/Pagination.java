package com.melonbar.exchange.coinbase.util.request;

import com.melonbar.exchange.coinbase.util.Guard;

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

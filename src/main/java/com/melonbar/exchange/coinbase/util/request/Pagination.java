package com.melonbar.exchange.coinbase.util.request;

import com.melonbar.exchange.coinbase.util.Guard;

public record Pagination(Cursor cursor, int page, int limit) {

    public enum Cursor {
        BEFORE,
        AFTER;
    }

    public static Pagination after(final int page, final int limit) {
        return new Pagination(Cursor.AFTER, page, limit);
    }

    public static Pagination before(final int page, final int limit) {
        return new Pagination(Cursor.BEFORE, page, limit);
    }

    public String toString() {
        Guard.nonNull(cursor);
        return cursor.name().toLowerCase() + "=" + page + "&limit=" + limit;
    }
}

package com.coinbase.exchange.model;

public final record Field(String name, String description, Class<?> type, boolean required) {

    private static final String NO_DESCRIPTION = "No description.";

    public static Field of(final String name, final Class<?> type) {
        return of(name, NO_DESCRIPTION, type, true);
    }

    public static Field of(final String name, final String description, final Class<?> type) {
        return of(name, description, type, true);
    }

    public static Field of(final String name, final Class<?> type, final boolean required) {
        return of(name, NO_DESCRIPTION, type, required);
    }

    public static Field of(final String name, final String description, final Class<?> type, final boolean required) {
        return new Field(name, description, type, required);
    }
}

package com.melonbar.exchange.coinbase.model.products;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Granularity {

    ONE_MINUTE(60),
    FIVE_MINUTES(300),
    FIFTEEN_MINUTES(900),
    ONE_HOUR(3600),
    SIX_HOURS(21600),
    ONE_DAY(86400);

    private final Integer seconds;

    @Override
    public String toString() {
        return Integer.toString(seconds);
    }
}

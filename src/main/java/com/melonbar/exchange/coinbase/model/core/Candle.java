package com.melonbar.exchange.coinbase.model.core;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public record Candle(DateTime startTime, BigDecimal low, BigDecimal high,
                     BigDecimal open, BigDecimal close, BigDecimal volume) {

    public static Candle from(final String bucket) {
        final String[] fields = bucket.substring(1, bucket.length()-2).split(",");
        if (fields.length != 6) {
            throw new IllegalArgumentException("Invalid number of detected fields for candle: " + bucket);
        }
        return new Candle(new DateTime(Long.parseLong(fields[0])*1000),
                new BigDecimal(fields[1]), new BigDecimal(fields[2]),
                new BigDecimal(fields[3]), new BigDecimal(fields[4]),
                new BigDecimal(fields[5]));
    }

    public Sentiment getType() {
        return close.compareTo(open) > 0 ? Sentiment.BULLISH : Sentiment.BEARISH;
    }
}

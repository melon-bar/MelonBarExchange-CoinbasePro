package com.melonbar.exchange.coinbase.model.core;

import com.melonbar.exchange.coinbase.util.Guard;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.util.Comparator;

public record Candle(DateTime startTime, BigDecimal low, BigDecimal high,
                     BigDecimal open, BigDecimal close, BigDecimal volume)
        implements Comparable<Candle> {

    public static Candle from(final String bucket) {
        final String[] fields = bucket.substring(1, bucket.length()-2).split(",");
        if (fields.length != 6) {
            throw new IllegalArgumentException("Invalid number of detected fields for candle: " + bucket);
        }
        return new Candle(new DateTime(Long.parseLong(fields[0])*1000, DateTimeZone.UTC),
                new BigDecimal(fields[1]), new BigDecimal(fields[2]),
                new BigDecimal(fields[3]), new BigDecimal(fields[4]),
                new BigDecimal(fields[5]));
    }

    public static CandlesHighComparator highComparator() {
        return new CandlesHighComparator();
    }

    public static CandlesLowComparator lowComparator() {
        return new CandlesLowComparator();
    }

    public Sentiment getType() {
        return close.compareTo(open) > 0 ? Sentiment.BULLISH : Sentiment.BEARISH;
    }

    public long differenceInMillis(final Candle candle) {
        return startTime().getMillis() - candle.startTime().getMillis();
    }

    @Override
    public int compareTo(final Candle candle) {
        return candle != null ? this.startTime.compareTo(candle.startTime) : 1;
    }

    private static class CandlesHighComparator implements Comparator<Candle> {

        @Override
        public int compare(final Candle o1, final Candle o2) {
            Guard.nonNull(o1, o2);
            return o1.high.compareTo(o2.high);
        }
    }

    private static class CandlesLowComparator implements Comparator<Candle> {

        @Override
        public int compare(final Candle o1, final Candle o2) {
            Guard.nonNull(o1, o2);
            return -o1.low.compareTo(o2.low);
        }
    }
}

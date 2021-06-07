package com.melonbar.exchange.coinbase.model.core;

import com.melonbar.exchange.coinbase.util.Format;
import lombok.Getter;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CandlesRange {

    private final Candle[] candles;

    private final long secondsWidth;
    @Getter private final DateTime start;
    @Getter private final DateTime end;

    // pivot points
    private BigDecimal pivotPoint;
    private BigDecimal firstSupport;
    private BigDecimal firstResistance;
    private BigDecimal secondSupport;
    private BigDecimal secondResistance;

    // local variables cache for further calculations
    private BigDecimal globalHigh;
    private BigDecimal globalLow;

    public CandlesRange(final Candle[] candles) {
        if (candles.length < 2) {
            throw new IllegalArgumentException("Candle ranges must consist of at least 2 candles.");
        }

        // create local immutable copy of candles then sort in ascending order
        this.candles = Arrays.copyOf(candles, candles.length);
        Arrays.sort(candles);

        // compute width and validate its consistency across all candles
        final long millisWidth = computeMillisCandleWidth(candles);

        // validation checks
        validateConsistentCandleWidth(candles, millisWidth);

        // store time range and seconds width
        this.secondsWidth = millisWidth / 1000;
        this.start = candles[0].startTime();
        this.end = candles[candles.length-1].startTime();
    }

    public Candle[] getCandles() {
        return Arrays.copyOf(candles, candles.length);
    }

    public Candle getCandle(final int i) {
        return candles[i];
    }

    public long getSecondsWidth() {
        return secondsWidth;
    }

    public BigDecimal getPivotPoint() {
        return fromLocalCache(pivotPoint,
                // PP = (high + low + close) / 3
                () -> getGlobalHigh()
                        .add(getGlobalLow())
                        .add(candles[candles.length-1].close())
                        .divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP));
    }

    public BigDecimal getFirstResistance() {
        return fromLocalCache(firstResistance,
                // R1 = 2*PP - low
                () -> getPivotPoint()
                        .multiply(BigDecimal.valueOf(2))
                        .subtract(getGlobalLow()));
    }

    public BigDecimal getFirstSupport() {
        return fromLocalCache(firstSupport,
                // S1 = 2*PP - high
                () -> getPivotPoint()
                        .multiply(BigDecimal.valueOf(2))
                        .subtract(getGlobalHigh()));
    }

    public BigDecimal getSecondResistance() {
        return fromLocalCache(secondResistance,
                // R2 = PP + (high - low)
                () -> getPivotPoint()
                        .add(getGlobalHigh())
                        .subtract(getGlobalLow()));
    }

    public BigDecimal getSecondSupport() {
        return fromLocalCache(secondSupport,
                // S2 = PP - (high - low) = PP - high + low
                () -> getPivotPoint()
                        .subtract(getGlobalHigh())
                        .add(getGlobalLow()));
    }

    @Override
    public String toString() {
        return Format.format("{}[candles={}, granularity={}, start={}, end={}]", getClass().getSimpleName(),
                candles.length, secondsWidth, start, end);
    }

    public BigDecimal getGlobalHigh() {
        return fromLocalCache(globalHigh,
                () -> Collections.max(List.of(candles), Candle.highComparator()).high());
    }

    public BigDecimal getGlobalLow() {
        return fromLocalCache(globalLow,
                () -> Collections.max(List.of(candles), Candle.lowComparator()).low());
    }

    private long computeMillisCandleWidth(final Candle[] candles) {
        return candles[2].startTime().getMillis() - candles[1].startTime().getMillis();
    }

    private void validateConsistentCandleWidth(final Candle[] candles, final long width) {
        for (int i = 0; i < candles.length-1; i++) {
            final long diff = candles[i+1].differenceInMillis(candles[i]);
            if (diff != width) {
                throw new IllegalStateException(
                        Format.format("Candles [{}] and [{}] expected to have millis width [{}], " +
                                "but actually have width: [{}]", candles[i+1], candles[i], diff, width));
            }
        }
    }

    private <T> T fromLocalCache(T cachable, final Supplier<T> valueSupplier) {
        if (cachable == null) {
            cachable = valueSupplier.get();
        }
        return cachable;
    }
}

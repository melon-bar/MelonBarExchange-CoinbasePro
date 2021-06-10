package com.melonbar.exchange.coinbase.util.response;

import com.melonbar.exchange.coinbase.model.core.Candle;
import com.melonbar.exchange.coinbase.model.response.PostProcessor;

import java.util.Arrays;

public final class Candles {

    private static final String CANDLES_DELIMITER = "(?<=],)";

    public static PostProcessor<Candle[]> getCandles() {
        return response -> Arrays.stream(parseCandles(response.content()))
                .map(Candle::from)
                .toArray(Candle[]::new);
    }

    private static String[] parseCandles(final String body) {
        return body.substring(1, body.length()-1).split(CANDLES_DELIMITER);
    }
}

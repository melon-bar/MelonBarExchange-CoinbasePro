package com.melonbar.exchange.coinbase.model.core;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Currency {

    @Getter
    @RequiredArgsConstructor
    public enum Unit {
        BTC("Bitcoin"),
        ETH("Ethereum"),
        USD("US Dollar");

        private final String label;

        @JsonValue
        public String toJson() {
            return toString();
        }
    }
}

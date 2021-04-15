package com.coinbase.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Currency {

    @Getter
    @RequiredArgsConstructor
    enum Unit {
        BTC("Bitcoin"),
        ETH("Ethereum"),
        USD("US Dollar");

        private final String label;
    }
}

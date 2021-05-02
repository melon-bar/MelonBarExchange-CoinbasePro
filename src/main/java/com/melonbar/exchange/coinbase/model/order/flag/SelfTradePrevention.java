package com.melonbar.exchange.coinbase.model.order.flag;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SelfTradePrevention {
    DECREASE_AND_CANCEL("dc"),
    CANCEL_OLDEST("co"),
    CANCEL_NEWEST("cn"),
    CANCEL_BOTH("cb");

    private final String code;
}

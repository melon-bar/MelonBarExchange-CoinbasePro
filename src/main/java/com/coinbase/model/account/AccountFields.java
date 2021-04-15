package com.coinbase.model.account;

import com.coinbase.model.Field;

import java.math.BigDecimal;

public record AccountFields() {

    public static final Field ID = Field.of("id", "Account ID.",
            String.class);

    public static final Field CURRENCY = Field.of("currency", "The currency of the account.",
            String.class);

    public static final Field BALANCE = Field.of("balance", "Total funds in the account.",
            BigDecimal.class);

    public static final Field HOLDS = Field.of("holds", "Funds on hold (not available for use).",
            BigDecimal.class);

    public static final Field AVAILABLE = Field.of("available", "Funds available to withdraw or trade.",
            BigDecimal.class);

    public static final Field TRADING_ENABLED = Field.of("trading_enabled", "Is trading enabled for this account?",
            Boolean.class);
}

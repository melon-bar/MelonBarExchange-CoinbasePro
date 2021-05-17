package com.melonbar.exchange.coinbase.model.core;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@SuppressWarnings("ALL")
public class Currency {

    private static final char SHADER = '_';

    @Getter
    public enum Unit {

        AAVE    ("Aave"),
        ADA     ("Cardano"),
        ALGO    ("Algorand"),
        ANKR    ("Ankr"),
        ATOM    ("Cosmos"),
        BAL     ("Balancer"),
        BAND    ("Band Protocol"),
        BAT     ("Basic Attention Token"),
        BCH     ("Bitcoin Cash"),
        BNT     ("Bancor"),
        BTC     ("Bitcoin"),
        CGLD    ("Celo"),
        COMP    ("Compound"),
        CRV     ("Curve DAO Token"),
        CTSI    ("Cartesi"),
        CVC     ("Civic"),
        DAI     ("Dai"),
        DASH    ("Dash"),
        DNT     ("district0x"),
        ENJ     ("Enjin Coin"),
        EOS     ("EOS"),
        ETC     ("Ethereum Classic"),
        ETH     ("Ethereum"),
        FIL     ("Filecoin"),
        FORTH   ("Ampleforth Governance Token"),
        GRT     ("The Graph"),
        ICP     ("Internet Computer"),
        KNC     ("Kyber Network Crystal Legacy"),
        LINK    ("Chainlink"),
        LOOM    ("Loom Network"),
        LRC     ("Loopring"),
        LTC     ("Litecoin"),
        MANA    ("Decentraland"),
        MATIC   ("Polygon"),
        MIR     ("Mirror Protocol"),
        MKR     ("Maker"),
        NKN     ("NKN"),
        NMR     ("Numeraire"),
        NU      ("NuCypher"),
        OGN     ("Origin Protocol"),
        OXT     ("Orchid"),
        PRC     ("Loopring"),
        REN     ("Ren"),
        REP     ("Augur"),
        RLC     ("iExec RLC"),
        SKL     ("SKALE Network"),
        SNX     ("Synthetix"),
        STORJ   ("Storj"),
        SUSHI   ("SushiSwap"),
        TRB     ("Tellor"),
        UMA     ("UMA"),
        UNI     ("Uniswap"),
        USD     ("US Dollar"),
        USDC    ("USD Coin"),
        USDT    ("Tether"),
        WBTC    ("Wrapped Bitcoin"),
        XLM     ("Stellar"),
        XRP     ("XRP"),
        XTZ     ("Tezos"),
        YFI     ("yearn.finance"),
        ZEC     ("Zcash"),
        ZRX     ("Ox"),

        // currencies that start with illegal variable characters must have shaded names
        _1INCH  ("1INCH", "1INCH");

        private final String label;
        private final String symbol;

        Unit(final String label) {
            this.label = label;
            symbol = name();
        }

        Unit(final String label, final String override) {
            this.label = label;
            symbol = override;
        }

        public static Unit unitOf(final String symbol) {
            try {
                return valueOf(symbol.toUpperCase());
            } catch (IllegalArgumentException illegalArgumentException) {
                // try applying shader for edge case coins such as 1INCH
                return valueOf(SHADER + symbol.toUpperCase());
            }
        }

        @JsonValue
        public String toJson() {
            return toString();
        }

        @Override
        public String toString() {
            return symbol;
        }
    }
}

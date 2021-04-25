package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.BodyField;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class MarketOrderRequest extends BaseNewOrderRequest {

    @BodyField(key = "size")
    private final BigDecimal orderSize;

    @BodyField(key = "funds")
    private final BigDecimal funds;

    @Override
    public boolean validateRequest() {
        return !(orderSize != null && funds != null);
    }
}

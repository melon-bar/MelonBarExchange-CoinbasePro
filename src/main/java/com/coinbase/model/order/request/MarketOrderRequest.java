package com.coinbase.model.order.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class MarketOrderRequest extends BaseNewOrderRequest {

    private BigDecimal orderSize;
    private BigDecimal funds;

    @Override
    public boolean validateRequest() {
        return !(orderSize != null && funds != null);
    }
}

package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.RequestField;
import lombok.Builder;

@Builder
public class GetOrderByApiKeyRequest extends BaseNewOrderRequest {

    @RequestField(index = 0)
    private final String apiKey;
}

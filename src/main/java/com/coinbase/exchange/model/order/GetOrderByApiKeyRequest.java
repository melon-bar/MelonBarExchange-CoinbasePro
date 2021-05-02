package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.RequestField;
import com.coinbase.exchange.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class GetOrderByApiKeyRequest extends BaseRequest {

    @RequestField(index = 0)
    private final String apiKey;
}

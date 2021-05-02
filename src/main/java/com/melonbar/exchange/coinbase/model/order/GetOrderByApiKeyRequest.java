package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class GetOrderByApiKeyRequest extends BaseRequest {

    @RequestField(index = 0)
    private final String apiKey;
}

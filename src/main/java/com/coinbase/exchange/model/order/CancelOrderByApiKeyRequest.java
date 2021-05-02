package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.BodyField;
import com.coinbase.exchange.annotation.RequestField;
import com.coinbase.exchange.model.request.BaseRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class CancelOrderByApiKeyRequest extends BaseRequest {

    @BodyField(key = "product_id")
    private final String productId;

    @RequestField(index = 0)
    @Getter @Setter private String apiKey;
}

package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
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

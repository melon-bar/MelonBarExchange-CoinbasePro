package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.QueryField;
import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.core.http.request.BaseRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class CancelOrderByApiKeyRequest extends BaseRequest {

    @QueryField(key = "product_id")
    private final String productId;

    @RequestField(index = 0)
    @Getter @Setter private String apiKey;
}

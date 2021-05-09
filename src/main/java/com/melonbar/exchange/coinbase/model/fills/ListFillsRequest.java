package com.melonbar.exchange.coinbase.model.fills;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class ListFillsRequest extends BaseRequest {

    @BodyField(key = "order_id")
    private final String orderId;

    @BodyField(key = "product_id")
    private final ProductId productId;

    @Override
    public boolean isValidRequest() {
        return orderId != null || productId != null;
    }
}

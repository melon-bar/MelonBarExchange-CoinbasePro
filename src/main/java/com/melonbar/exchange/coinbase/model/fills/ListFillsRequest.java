package com.melonbar.exchange.coinbase.model.fills;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.model.core.Product;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class ListFillsRequest extends BaseRequest {

    @BodyField(key = "order_id")
    private final String orderId;

    @BodyField(key = "product_id")
    private final Product product;

    @Override
    public boolean isValidRequest() {
        return orderId != null || product != null;
    }
}

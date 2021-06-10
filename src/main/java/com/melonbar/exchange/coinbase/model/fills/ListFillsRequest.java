package com.melonbar.exchange.coinbase.model.fills;

import com.melonbar.exchange.coinbase.annotation.QueryField;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class ListFillsRequest extends BaseRequest {

    @QueryField(key = "order_id")
    private final String orderId;

    @QueryField(key = "product_id")
    private final ProductId productId;

    @Override
    public boolean isValidRequest() {
        return orderId != null || productId != null;
    }
}

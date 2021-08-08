package com.melonbar.exchange.coinbase.model.fills;

import com.melonbar.core.http.request.BaseRequest;
import com.melonbar.core.model.ProductId;
import com.melonbar.exchange.coinbase.annotation.QueryField;
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

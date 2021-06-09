package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.QueryField;
import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class CancelOrderByOrderIdRequest extends BaseRequest {

    @QueryField(key = "product_id")
    private final String productId;

    @RequestField(index = 0)
    private final String orderId;
}

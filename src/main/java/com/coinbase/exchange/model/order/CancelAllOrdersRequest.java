package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.BodyField;
import com.coinbase.exchange.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class CancelAllOrdersRequest extends BaseRequest {

    @BodyField(key = "product_id")
    private final String productId;
}

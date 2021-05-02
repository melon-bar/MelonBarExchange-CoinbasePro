package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class CancelAllOrdersRequest extends BaseRequest {

    @BodyField(key = "product_id")
    private final String productId;
}

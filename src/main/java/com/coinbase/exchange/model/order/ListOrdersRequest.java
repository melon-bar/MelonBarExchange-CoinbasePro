package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.BodyField;
import com.coinbase.exchange.model.order.flag.OrderStatus;
import com.coinbase.exchange.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class ListOrdersRequest extends BaseRequest {

    @BodyField(key = "status")
    private final OrderStatus[] orderStatuses;

    @BodyField(key = "product_id")
    private final String productId;
}

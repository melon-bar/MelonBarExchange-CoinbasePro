package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.model.order.flag.OrderStatus;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class ListOrdersRequest extends BaseRequest {

    @BodyField(key = "status")
    private final OrderStatus[] orderStatuses;

    @BodyField(key = "product_id")
    private final String productId;
}

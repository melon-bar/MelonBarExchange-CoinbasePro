package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.QueryField;
import com.melonbar.exchange.coinbase.model.order.flag.OrderStatus;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class ListOrdersRequest extends BaseRequest {

    @QueryField(key = "status")
    private final OrderStatus[] orderStatuses;

    @QueryField(key = "product_id")
    private final String productId;
}

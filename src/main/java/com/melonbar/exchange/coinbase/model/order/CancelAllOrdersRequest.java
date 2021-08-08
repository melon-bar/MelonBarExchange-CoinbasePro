package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.QueryField;
import com.melonbar.core.http.request.BaseRequest;
import lombok.Builder;

@Builder
public class CancelAllOrdersRequest extends BaseRequest {

    @QueryField(key = "product_id")
    private final String productId;
}

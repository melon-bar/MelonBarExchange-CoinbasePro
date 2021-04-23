package com.coinbase.exchange.model.order.request;

import com.coinbase.exchange.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class CancelOrderRequest extends BaseRequest {
    private String productId;
}

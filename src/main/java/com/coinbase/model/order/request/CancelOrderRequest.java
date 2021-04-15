package com.coinbase.model.order.request;

import com.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class CancelOrderRequest extends BaseRequest {
    private String productId;
}

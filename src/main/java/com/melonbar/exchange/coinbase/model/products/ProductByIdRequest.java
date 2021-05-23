package com.melonbar.exchange.coinbase.model.products;

import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class ProductByIdRequest extends BaseRequest {

    @RequestField(index = 0)
    private final ProductId productId;
}

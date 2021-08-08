package com.melonbar.exchange.coinbase.model.products;

import com.melonbar.core.http.request.BaseRequest;
import com.melonbar.core.model.ProductId;
import com.melonbar.exchange.coinbase.annotation.RequestField;
import lombok.Builder;

@Builder
public class ProductByIdRequest extends BaseRequest {

    @RequestField(index = 0)
    private final ProductId productId;
}

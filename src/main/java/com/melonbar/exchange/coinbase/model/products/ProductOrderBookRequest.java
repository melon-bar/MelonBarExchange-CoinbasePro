package com.melonbar.exchange.coinbase.model.products;

import com.melonbar.exchange.coinbase.annotation.QueryField;
import com.melonbar.core.http.request.BaseRequest;
import lombok.Builder;

@Builder
public class ProductOrderBookRequest extends BaseRequest {

    @QueryField(key = "level")
    private final Integer level;
}

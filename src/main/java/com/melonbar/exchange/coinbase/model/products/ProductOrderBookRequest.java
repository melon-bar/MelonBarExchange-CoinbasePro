package com.melonbar.exchange.coinbase.model.products;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class ProductOrderBookRequest extends BaseRequest {

    @BodyField(key = "level")
    private final Integer level;
}

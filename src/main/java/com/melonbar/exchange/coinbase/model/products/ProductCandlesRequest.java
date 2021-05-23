package com.melonbar.exchange.coinbase.model.products;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;
import org.joda.time.DateTime;

@Builder
public class ProductCandlesRequest extends BaseRequest {

    @RequestField(index = 0)
    public ProductId productId;

    @BodyField(key = "start")
    public DateTime startTime;

    @BodyField(key = "end")
    public DateTime endTime;

    @BodyField(key = "granularity")
    public Granularity granularity;
}

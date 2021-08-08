package com.melonbar.exchange.coinbase.model.products;

import com.melonbar.core.model.ProductId;
import com.melonbar.exchange.coinbase.annotation.QueryField;
import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.core.http.request.BaseRequest;
import lombok.Builder;
import org.joda.time.DateTime;

@Builder
public class ProductCandlesRequest extends BaseRequest {

    @RequestField(index = 0)
    public ProductId productId;

    @QueryField(key = "start")
    public DateTime startTime;

    @QueryField(key = "end")
    public DateTime endTime;

    @QueryField(key = "granularity")
    public Granularity granularity;
}

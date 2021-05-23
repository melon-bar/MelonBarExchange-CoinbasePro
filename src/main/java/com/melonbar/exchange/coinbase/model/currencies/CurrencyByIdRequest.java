package com.melonbar.exchange.coinbase.model.currencies;

import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.exchange.coinbase.model.core.Currency;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;

@Builder
public class CurrencyByIdRequest extends BaseRequest {

    @RequestField(index = 0)
    private final Currency.Unit unitId;
}

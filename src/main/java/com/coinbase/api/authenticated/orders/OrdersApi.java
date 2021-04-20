package com.coinbase.api.authenticated.orders;

import com.coinbase.model.Response;
import com.coinbase.model.order.request.LimitOrderRequest;
import com.coinbase.model.order.request.MarketOrderRequest;

public interface OrdersApi {

    Response placeLimitOrder(final LimitOrderRequest limitOrderRequest);

    Response placeMarketOrder(final MarketOrderRequest marketOrderRequest);
}

package com.coinbase.exchange.api.authenticated.orders;

import com.coinbase.exchange.model.Response;
import com.coinbase.exchange.model.order.request.LimitOrderRequest;
import com.coinbase.exchange.model.order.request.MarketOrderRequest;

public interface OrdersApi {

    Response placeLimitOrder(final LimitOrderRequest limitOrderRequest);

    Response placeMarketOrder(final MarketOrderRequest marketOrderRequest);
}

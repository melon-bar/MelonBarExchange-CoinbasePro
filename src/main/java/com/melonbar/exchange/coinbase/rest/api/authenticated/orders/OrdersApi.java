package com.melonbar.exchange.coinbase.rest.api.authenticated.orders;

import com.melonbar.core.http.response.Response;
import com.melonbar.exchange.coinbase.model.order.CancelAllOrdersRequest;
import com.melonbar.exchange.coinbase.model.order.CancelOrderByApiKeyRequest;
import com.melonbar.exchange.coinbase.model.order.CancelOrderByOrderIdRequest;
import com.melonbar.exchange.coinbase.model.order.GetOrderByApiKeyRequest;
import com.melonbar.exchange.coinbase.model.order.GetOrderByOrderIdRequest;
import com.melonbar.exchange.coinbase.model.order.LimitOrderRequest;
import com.melonbar.exchange.coinbase.model.order.ListOrdersRequest;
import com.melonbar.exchange.coinbase.model.order.MarketOrderRequest;

public interface OrdersApi {

    Response placeLimitOrder(final LimitOrderRequest limitOrderRequest);

    Response placeMarketOrder(final MarketOrderRequest marketOrderRequest);

    Response cancelOrderByApiKey(final CancelOrderByApiKeyRequest cancelOrderByApiKeyRequest);

    Response cancelOrderByOrderId(final CancelOrderByOrderIdRequest cancelOrderByOrderIdRequest);

    Response cancelAllOrders(final CancelAllOrdersRequest cancelAllOrdersRequest);

    Response listOrders(final ListOrdersRequest listOrdersRequest);

    Response getOrderByApiKey(final GetOrderByApiKeyRequest getOrderByApiKeyRequest);

    Response getOrderByOrderId(final GetOrderByOrderIdRequest getOrderByOrderIdRequest);
}

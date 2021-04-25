package com.coinbase.exchange.api.authenticated.orders;

import com.coinbase.exchange.annotation.EnrichRequest;
import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.http.HttpClient;
import com.coinbase.exchange.model.Response;
import com.coinbase.exchange.model.order.CancelAllOrdersRequest;
import com.coinbase.exchange.model.order.CancelOrderByApiKeyRequest;
import com.coinbase.exchange.model.order.CancelOrderByOrderIdRequest;
import com.coinbase.exchange.model.order.GetOrderByApiKeyRequest;
import com.coinbase.exchange.model.order.GetOrderByOrderIdRequest;
import com.coinbase.exchange.model.order.LimitOrderRequest;
import com.coinbase.exchange.model.order.ListOrdersRequest;
import com.coinbase.exchange.model.order.MarketOrderRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrdersApiImpl implements OrdersApi {

    private final HttpClient httpClient;

    @Override
    @EnrichRequest(authority = Resource.PLACE_ORDER, type = Http.POST)
    public Response placeLimitOrder(final LimitOrderRequest limitOrderRequest) {
        return httpClient.send(limitOrderRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.PLACE_ORDER, type = Http.POST)
    public Response placeMarketOrder(final MarketOrderRequest marketOrderRequest) {
        return httpClient.send(marketOrderRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.CANCEL_ORDER_BY_API_KEY, type = Http.DELETE)
    public Response cancelOrderByApiKey(final CancelOrderByApiKeyRequest cancelOrderByApiKeyRequest) {
        return httpClient.send(cancelOrderByApiKeyRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.CANCEL_ORDER_BY_ORDER_ID, type = Http.DELETE)
    public Response cancelOrderByClientOrderId(final CancelOrderByOrderIdRequest cancelOrderByOrderIdRequest) {
        return httpClient.send(cancelOrderByOrderIdRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.CANCEL_ALL_ORDERS, type = Http.DELETE)
    public Response cancelAllOrders(final CancelAllOrdersRequest cancelAllOrdersRequest) {
        return httpClient.send(cancelAllOrdersRequest).body();
    }

    @Override
    public Response listOrders(final ListOrdersRequest listOrdersRequest) {
        return httpClient.send(listOrdersRequest).body();
    }

    @Override
    public Response getOrderByApiKey(final GetOrderByApiKeyRequest getOrderByApiKeyRequest) {
        return httpClient.send(getOrderByApiKeyRequest).body();
    }

    @Override
    public Response getOrderByOrderId(final GetOrderByOrderIdRequest getOrderByOrderIdRequest) {
        return httpClient.send(getOrderByOrderIdRequest).body();
    }
}

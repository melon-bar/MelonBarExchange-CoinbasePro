package com.coinbase.exchange.api.authenticated.orders;

import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.enrichment.Enricher;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.http.HttpClient;
import com.coinbase.exchange.model.response.Response;
import com.coinbase.exchange.model.order.CancelAllOrdersRequest;
import com.coinbase.exchange.model.order.CancelOrderByApiKeyRequest;
import com.coinbase.exchange.model.order.CancelOrderByOrderIdRequest;
import com.coinbase.exchange.model.order.GetOrderByApiKeyRequest;
import com.coinbase.exchange.model.order.GetOrderByOrderIdRequest;
import com.coinbase.exchange.model.order.LimitOrderRequest;
import com.coinbase.exchange.model.order.ListOrdersRequest;
import com.coinbase.exchange.model.order.MarketOrderRequest;

public record OrdersApiImpl(HttpClient httpClient, Enricher requestEnricher) implements OrdersApi {

    @Override
    public Response placeLimitOrder(final LimitOrderRequest limitOrderRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        limitOrderRequest, Http.POST, Resource.ORDER))
                .body();
    }

    @Override
    public Response placeMarketOrder(final MarketOrderRequest marketOrderRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        marketOrderRequest, Http.POST, Resource.ORDER))
                .body();
    }

    @Override
    public Response cancelOrderByApiKey(final CancelOrderByApiKeyRequest cancelOrderByApiKeyRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        cancelOrderByApiKeyRequest, Http.DELETE, Resource.ORDER_BY_API_KEY))
                .body();
    }

    @Override
    public Response cancelOrderByOrderId(final CancelOrderByOrderIdRequest cancelOrderByOrderIdRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        cancelOrderByOrderIdRequest, Http.DELETE, Resource.ORDER_BY_ORDER_ID))
                .body();
    }

    @Override
    public Response cancelAllOrders(final CancelAllOrdersRequest cancelAllOrdersRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        cancelAllOrdersRequest, Http.DELETE, Resource.ORDER))
                .body();
    }

    @Override
    public Response listOrders(final ListOrdersRequest listOrdersRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        listOrdersRequest, Http.GET, Resource.ORDER))
                .body();
    }

    @Override
    public Response getOrderByApiKey(final GetOrderByApiKeyRequest getOrderByApiKeyRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        getOrderByApiKeyRequest, Http.GET, Resource.ORDER_BY_API_KEY))
                .body();
    }

    @Override
    public Response getOrderByOrderId(final GetOrderByOrderIdRequest getOrderByOrderIdRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        getOrderByOrderIdRequest, Http.GET, Resource.ORDER_BY_API_KEY))
                .body();
    }
}

package com.coinbase.exchange.client;

import com.coinbase.exchange.api.authenticated.accounts.AccountsApi;
import com.coinbase.exchange.api.authenticated.orders.OrdersApi;
import com.coinbase.exchange.model.response.Response;
import com.coinbase.exchange.model.account.AccountsRequest;
import com.coinbase.exchange.model.order.CancelAllOrdersRequest;
import com.coinbase.exchange.model.order.CancelOrderByApiKeyRequest;
import com.coinbase.exchange.model.order.CancelOrderByOrderIdRequest;
import com.coinbase.exchange.model.order.GetOrderByApiKeyRequest;
import com.coinbase.exchange.model.order.GetOrderByOrderIdRequest;
import com.coinbase.exchange.model.order.LimitOrderRequest;
import com.coinbase.exchange.model.order.ListOrdersRequest;
import com.coinbase.exchange.model.order.MarketOrderRequest;
import com.coinbase.exchange.util.Guard;
import com.coinbase.exchange.util.request.Pagination;

public record CoinbaseProClientImpl(AccountsApi accountsApi, OrdersApi ordersApi) implements CoinbaseProClient {

    @Override
    public Response listAccounts(final AccountsRequest accountsRequest) {
        Guard.nonNull(accountsRequest);
        return accountsApi().listAccounts(accountsRequest);
    }

    @Override
    public Response getAccount(final AccountsRequest accountsRequest) {
        Guard.nonNull(accountsRequest);
        return accountsApi().getAccount(accountsRequest);
    }

    @Override
    public Response getAccountHistory(final AccountsRequest accountsRequest, final Pagination pagination) {
        Guard.nonNull(accountsRequest);
        accountsRequest.setPagination(pagination);
        return accountsApi().getAccountHistory(accountsRequest);
    }

    @Override
    public Response placeLimitOrder(final LimitOrderRequest limitOrderRequest) {
        Guard.nonNull(limitOrderRequest);
        return ordersApi().placeLimitOrder(limitOrderRequest);
    }

    @Override
    public Response placeMarketOrder(final MarketOrderRequest marketOrderRequest) {
        Guard.nonNull(marketOrderRequest);
        return ordersApi().placeMarketOrder(marketOrderRequest);
    }

    @Override
    public Response cancelOrderByApiKey(final CancelOrderByApiKeyRequest cancelOrderByApiKeyRequest) {
        Guard.nonNull(cancelOrderByApiKeyRequest);
        return ordersApi().cancelOrderByApiKey(cancelOrderByApiKeyRequest);
    }

    @Override
    public Response cancelOrderByOrderId(final CancelOrderByOrderIdRequest cancelOrderByOrderIdRequest) {
        Guard.nonNull(cancelOrderByOrderIdRequest);
        return ordersApi().cancelOrderByOrderId(cancelOrderByOrderIdRequest);
    }

    @Override
    public Response cancelAllOrders(final CancelAllOrdersRequest cancelAllOrdersRequest) {
        Guard.nonNull(cancelAllOrdersRequest);
        return ordersApi().cancelAllOrders(cancelAllOrdersRequest);
    }

    @Override
    public Response listOrders(final ListOrdersRequest listOrdersRequest, final Pagination pagination) {
        Guard.nonNull(listOrdersRequest);
        listOrdersRequest.setPagination(pagination);
        return ordersApi().listOrders(listOrdersRequest);
    }

    @Override
    public Response getOrderByApiKey(final GetOrderByApiKeyRequest getOrderByApiKeyRequest) {
        Guard.nonNull(getOrderByApiKeyRequest);
        return ordersApi().getOrderByApiKey(getOrderByApiKeyRequest);
    }

    @Override
    public Response getOrderByOrderId(final GetOrderByOrderIdRequest getOrderByOrderIdRequest) {
        Guard.nonNull(getOrderByOrderIdRequest);
        return ordersApi().getOrderByOrderId(getOrderByOrderIdRequest);
    }
}

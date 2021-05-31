package com.melonbar.exchange.coinbase.rest;

import com.melonbar.exchange.coinbase.rest.api.authenticated.accounts.AccountsApi;
import com.melonbar.exchange.coinbase.rest.api.authenticated.fills.FillsApi;
import com.melonbar.exchange.coinbase.rest.api.authenticated.oracle.OracleApi;
import com.melonbar.exchange.coinbase.rest.api.authenticated.orders.OrdersApi;
import com.melonbar.exchange.coinbase.rest.api.marketdata.MarketDataApi;
import com.melonbar.exchange.coinbase.model.fills.ListFillsRequest;
import com.melonbar.exchange.coinbase.model.oracle.OracleRequest;
import com.melonbar.exchange.coinbase.model.products.ProductCandlesRequest;
import com.melonbar.exchange.coinbase.model.response.Response;
import com.melonbar.exchange.coinbase.model.account.AccountsRequest;
import com.melonbar.exchange.coinbase.model.order.CancelAllOrdersRequest;
import com.melonbar.exchange.coinbase.model.order.CancelOrderByApiKeyRequest;
import com.melonbar.exchange.coinbase.model.order.CancelOrderByOrderIdRequest;
import com.melonbar.exchange.coinbase.model.order.GetOrderByApiKeyRequest;
import com.melonbar.exchange.coinbase.model.order.GetOrderByOrderIdRequest;
import com.melonbar.exchange.coinbase.model.order.LimitOrderRequest;
import com.melonbar.exchange.coinbase.model.order.ListOrdersRequest;
import com.melonbar.exchange.coinbase.model.order.MarketOrderRequest;
import com.melonbar.exchange.coinbase.util.Guard;
import com.melonbar.exchange.coinbase.util.request.Pagination;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CoinbaseProRestClientImpl implements CoinbaseProRestClient {

    private final AccountsApi accountsApi;
    private final OrdersApi ordersApi;
    private final FillsApi fillsApi;
    private final OracleApi oracleApi;

    private final MarketDataApi marketDataApi;

    // accounts

    @Override
    public Response listAccounts(final AccountsRequest accountsRequest) {
        Guard.nonNull(accountsRequest);
        return accountsApi.listAccounts(accountsRequest);
    }

    @Override
    public Response getAccount(final AccountsRequest accountsRequest) {
        Guard.nonNull(accountsRequest);
        return accountsApi.getAccount(accountsRequest);
    }

    @Override
    public Response getAccountHistory(final AccountsRequest accountsRequest, final Pagination pagination) {
        Guard.nonNull(accountsRequest);
        accountsRequest.setPagination(pagination);
        return accountsApi.getAccountHistory(accountsRequest);
    }

    // orders

    @Override
    public Response placeLimitOrder(final LimitOrderRequest limitOrderRequest) {
        Guard.nonNull(limitOrderRequest);
        return ordersApi.placeLimitOrder(limitOrderRequest);
    }

    @Override
    public Response placeMarketOrder(final MarketOrderRequest marketOrderRequest) {
        Guard.nonNull(marketOrderRequest);
        return ordersApi.placeMarketOrder(marketOrderRequest);
    }

    @Override
    public Response cancelOrderByApiKey(final CancelOrderByApiKeyRequest cancelOrderByApiKeyRequest) {
        Guard.nonNull(cancelOrderByApiKeyRequest);
        return ordersApi.cancelOrderByApiKey(cancelOrderByApiKeyRequest);
    }

    @Override
    public Response cancelOrderByOrderId(final CancelOrderByOrderIdRequest cancelOrderByOrderIdRequest) {
        Guard.nonNull(cancelOrderByOrderIdRequest);
        return ordersApi.cancelOrderByOrderId(cancelOrderByOrderIdRequest);
    }

    @Override
    public Response cancelAllOrders(final CancelAllOrdersRequest cancelAllOrdersRequest) {
        Guard.nonNull(cancelAllOrdersRequest);
        return ordersApi.cancelAllOrders(cancelAllOrdersRequest);
    }

    @Override
    public Response listOrders(final ListOrdersRequest listOrdersRequest, final Pagination pagination) {
        Guard.nonNull(listOrdersRequest);
        listOrdersRequest.setPagination(pagination);
        return ordersApi.listOrders(listOrdersRequest);
    }

    @Override
    public Response getOrderByApiKey(final GetOrderByApiKeyRequest getOrderByApiKeyRequest) {
        Guard.nonNull(getOrderByApiKeyRequest);
        return ordersApi.getOrderByApiKey(getOrderByApiKeyRequest);
    }

    @Override
    public Response getOrderByOrderId(final GetOrderByOrderIdRequest getOrderByOrderIdRequest) {
        Guard.nonNull(getOrderByOrderIdRequest);
        return ordersApi.getOrderByOrderId(getOrderByOrderIdRequest);
    }

    // fills

    @Override
    public Response listFills(final ListFillsRequest listFillsRequest, final Pagination pagination) {
        Guard.nonNull(listFillsRequest);
        listFillsRequest.setPagination(pagination);
        return fillsApi.listFills(listFillsRequest);
    }

    // oracle

    @Override
    public Response getOracle(final OracleRequest oracleRequest) {
        Guard.nonNull(oracleRequest);
        return oracleApi.getOracle(oracleRequest);
    }

    // market data

    @Override
    public Response getProductCandles(final ProductCandlesRequest productCandlesRequest) {
        Guard.nonNull(productCandlesRequest);
        return marketDataApi.getProductCandles(productCandlesRequest);
    }
}

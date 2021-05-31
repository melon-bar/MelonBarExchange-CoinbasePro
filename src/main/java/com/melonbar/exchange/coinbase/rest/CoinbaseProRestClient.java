package com.melonbar.exchange.coinbase.rest;

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
import com.melonbar.exchange.coinbase.util.request.Pagination;

public interface CoinbaseProRestClient {

    Response listAccounts(final AccountsRequest accountsRequest);

    Response getAccount(final AccountsRequest accountsRequest);

    Response getAccountHistory(final AccountsRequest accountsRequest, final Pagination pagination);

    Response placeLimitOrder(final LimitOrderRequest limitOrderRequest);

    Response placeMarketOrder(final MarketOrderRequest marketOrderRequest);

    Response cancelOrderByApiKey(final CancelOrderByApiKeyRequest cancelOrderByApiKeyRequest);

    Response cancelOrderByOrderId(final CancelOrderByOrderIdRequest cancelOrderByOrderIdRequest);

    Response cancelAllOrders(final CancelAllOrdersRequest cancelAllOrdersRequest);

    Response listOrders(final ListOrdersRequest listOrdersRequest, final Pagination pagination);

    Response getOrderByApiKey(final GetOrderByApiKeyRequest getOrderByApiKeyRequest);

    Response getOrderByOrderId(final GetOrderByOrderIdRequest getOrderByOrderIdRequest);

    Response getOracle(final OracleRequest oracleRequest);

    Response listFills(final ListFillsRequest listFillsRequest, final Pagination pagination);

    Response getProductCandles(final ProductCandlesRequest productCandlesRequest);
}

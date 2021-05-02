package com.coinbase.exchange.client;

import com.coinbase.exchange.model.Response;
import com.coinbase.exchange.model.account.AccountsRequest;
import com.coinbase.exchange.model.order.CancelAllOrdersRequest;
import com.coinbase.exchange.model.order.CancelOrderByApiKeyRequest;
import com.coinbase.exchange.model.order.CancelOrderByOrderIdRequest;
import com.coinbase.exchange.model.order.GetOrderByApiKeyRequest;
import com.coinbase.exchange.model.order.GetOrderByOrderIdRequest;
import com.coinbase.exchange.model.order.LimitOrderRequest;
import com.coinbase.exchange.model.order.ListOrdersRequest;
import com.coinbase.exchange.model.order.MarketOrderRequest;
import com.coinbase.exchange.util.request.Pagination;

public interface CoinbaseProClient {

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
}

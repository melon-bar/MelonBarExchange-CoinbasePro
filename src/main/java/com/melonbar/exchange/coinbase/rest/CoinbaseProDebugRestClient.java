package com.melonbar.exchange.coinbase.rest;

import com.melonbar.core.http.Http;
import com.melonbar.core.http.HttpClient;
import com.melonbar.exchange.coinbase.model.request.DebugRequest;
import com.melonbar.core.http.response.Response;
import com.melonbar.exchange.coinbase.rest.api.authenticated.accounts.AccountsApi;
import com.melonbar.exchange.coinbase.rest.api.authenticated.fills.FillsApi;
import com.melonbar.exchange.coinbase.rest.api.authenticated.oracle.OracleApi;
import com.melonbar.exchange.coinbase.rest.api.authenticated.orders.OrdersApi;
import com.melonbar.exchange.coinbase.rest.api.marketdata.MarketDataApi;

public class CoinbaseProDebugRestClient extends CoinbaseProRestClientImpl {

    private final HttpClient httpClient;

    protected CoinbaseProDebugRestClient(final HttpClient httpClient,
                                         final AccountsApi accountsApi,
                                         final OrdersApi ordersApi,
                                         final FillsApi fillsApi,
                                         final OracleApi oracleApi,
                                         final MarketDataApi marketDataApi) {
        super(accountsApi, ordersApi, fillsApi, oracleApi, marketDataApi);
        this.httpClient = httpClient;
    }

    public Response debugRequest(final String body, final String requestPath, final Http method) {
        return httpClient
                .send(new DebugRequest(body, requestPath, method))
                .body();
    }
}

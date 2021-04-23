package com.coinbase.exchange.client;

import com.coinbase.exchange.api.authenticated.accounts.AccountsApi;
import com.coinbase.exchange.api.authenticated.orders.OrdersApi;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public record CoinbaseProClientImpl(AccountsApi accountsApi, OrdersApi ordersApi) implements CoinbaseProClient {

}
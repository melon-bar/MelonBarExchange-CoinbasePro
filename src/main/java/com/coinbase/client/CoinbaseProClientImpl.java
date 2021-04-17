package com.coinbase.client;

import com.coinbase.api.authenticated.accounts.AccountsApi;
import com.coinbase.api.authenticated.orders.OrdersApi;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public record CoinbaseProClientImpl(AccountsApi accountsApi, OrdersApi ordersApi) implements CoinbaseProClient {

}

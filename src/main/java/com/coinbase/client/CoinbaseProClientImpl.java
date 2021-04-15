package com.coinbase.client;

import com.coinbase.api.accounts.AccountsApi;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoinbaseProClientImpl implements CoinbaseProClient {

    private final AccountsApi accountsApi;

}

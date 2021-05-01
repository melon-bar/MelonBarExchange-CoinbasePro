package com.coinbase.exchange.api.authenticated.accounts;

import com.coinbase.exchange.annotation.EnrichRequest;
import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.http.HttpClient;
import com.coinbase.exchange.model.Response;
import com.coinbase.exchange.model.account.AccountsRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountsApiImpl implements AccountsApi {

    private final HttpClient httpClient;

    @Override
    @EnrichRequest(authority = Resource.ACCOUNT, type = Http.GET)
    public Response listAccounts(final AccountsRequest accountsRequest) {
        return httpClient.send(accountsRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.ACCOUNT_BY_ID, type = Http.GET)
    public Response getAccount(final AccountsRequest accountsRequest) {
        return httpClient.send(accountsRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.ACCOUNT_LEDGER, type = Http.GET)
    public Response getAccountHistory(final AccountsRequest accountsRequest) {
        return httpClient.send(accountsRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.ACCOUNT_HOLDS, type = Http.GET)
    public Response getHolds(final AccountsRequest accountsRequest) {
        return httpClient.send(accountsRequest).body();
    }
}

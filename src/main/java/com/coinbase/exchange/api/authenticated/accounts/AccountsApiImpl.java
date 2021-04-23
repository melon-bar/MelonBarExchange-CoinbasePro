package com.coinbase.exchange.api.authenticated.accounts;

import com.coinbase.exchange.annotation.EnrichRequest;
import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.http.HttpClient;
import com.coinbase.exchange.model.Response;
import com.coinbase.exchange.model.account.AccountsRequest;
import lombok.RequiredArgsConstructor;

// TODO: annotation processing for enriching data before http request
@RequiredArgsConstructor
public class AccountsApiImpl implements AccountsApi {

    private final HttpClient httpClient;

    @Override
    @EnrichRequest(authority = Resource.LIST_ACCOUNTS, type = Http.GET)
    public Response listAccounts(final AccountsRequest accountsRequest) {
        return httpClient.send(accountsRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.GET_ACCOUNT, type = Http.GET)
    public Response getAccount(final AccountsRequest accountsRequest) {
        return httpClient.send(accountsRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.GET_ACCOUNT_HISTORY, type = Http.GET)
    public Response getAccountHistory(final AccountsRequest accountsRequest) {
        return httpClient.send(accountsRequest).body();
    }

    @Override
    @EnrichRequest(authority = Resource.GET_HOLDS, type = Http.GET)
    public Response getHolds(final AccountsRequest accountsRequest) {
        return httpClient.send(accountsRequest).body();
    }
}
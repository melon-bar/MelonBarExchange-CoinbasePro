package com.coinbase.api.authenticated.accounts;

import com.coinbase.annotation.EnrichRequest;
import com.coinbase.api.resource.Resource;
import com.coinbase.http.Http;
import com.coinbase.http.HttpClient;
import com.coinbase.model.Response;
import com.coinbase.model.account.AccountsRequest;
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

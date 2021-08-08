package com.melonbar.exchange.coinbase.rest.api.authenticated.accounts;

import com.melonbar.exchange.coinbase.rest.api.resource.Resource;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.core.http.Http;
import com.melonbar.core.http.HttpClient;
import com.melonbar.core.http.response.Response;
import com.melonbar.exchange.coinbase.model.account.AccountsRequest;

public record AccountsApiImpl(HttpClient httpClient, Enricher requestEnricher) implements AccountsApi {

    @Override
    public Response listAccounts(final AccountsRequest accountsRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        accountsRequest, Http.GET, Resource.ACCOUNT))
                .body();
    }

    @Override
    public Response getAccount(final AccountsRequest accountsRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        accountsRequest, Http.GET, Resource.ACCOUNT_BY_ID))
                .body();
    }

    @Override
    public Response getAccountHistory(final AccountsRequest accountsRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        accountsRequest, Http.GET, Resource.ACCOUNT_LEDGER))
                .body();
    }

    @Override
    public Response getHolds(final AccountsRequest accountsRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        accountsRequest, Http.GET, Resource.ACCOUNT_HOLDS))
                .body();
    }
}

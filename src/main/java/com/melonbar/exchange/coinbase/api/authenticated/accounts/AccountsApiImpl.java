package com.melonbar.exchange.coinbase.api.authenticated.accounts;

import com.melonbar.exchange.coinbase.api.resource.Resource;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.exchange.coinbase.http.Http;
import com.melonbar.exchange.coinbase.http.HttpClient;
import com.melonbar.exchange.coinbase.model.response.Response;
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

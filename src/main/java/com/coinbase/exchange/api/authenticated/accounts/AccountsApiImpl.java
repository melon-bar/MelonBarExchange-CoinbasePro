package com.coinbase.exchange.api.authenticated.accounts;

import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.enrichment.Enricher;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.http.HttpClient;
import com.coinbase.exchange.model.Response;
import com.coinbase.exchange.model.account.AccountsRequest;

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

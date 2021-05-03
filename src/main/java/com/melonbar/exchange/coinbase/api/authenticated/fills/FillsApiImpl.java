package com.melonbar.exchange.coinbase.api.authenticated.fills;

import com.melonbar.exchange.coinbase.api.resource.Resource;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.exchange.coinbase.http.Http;
import com.melonbar.exchange.coinbase.http.HttpClient;
import com.melonbar.exchange.coinbase.model.fills.ListFillsRequest;
import com.melonbar.exchange.coinbase.model.response.Response;

public record FillsApiImpl(HttpClient httpClient, Enricher requestEnricher) implements FillsApi {

    @Override
    public Response listFills(final ListFillsRequest listFillsRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        listFillsRequest, Http.GET, Resource.FILLS))
                .body();
    }
}

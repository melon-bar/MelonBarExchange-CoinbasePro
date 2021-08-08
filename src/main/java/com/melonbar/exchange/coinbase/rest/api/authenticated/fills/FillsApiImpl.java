package com.melonbar.exchange.coinbase.rest.api.authenticated.fills;

import com.melonbar.exchange.coinbase.rest.api.resource.Resource;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.core.http.Http;
import com.melonbar.core.http.HttpClient;
import com.melonbar.exchange.coinbase.model.fills.ListFillsRequest;
import com.melonbar.core.http.response.Response;

public record FillsApiImpl(HttpClient httpClient, Enricher requestEnricher) implements FillsApi {

    @Override
    public Response listFills(final ListFillsRequest listFillsRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        listFillsRequest, Http.GET, Resource.FILLS))
                .body();
    }
}

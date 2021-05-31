package com.melonbar.exchange.coinbase.rest.api.authenticated.oracle;

import com.melonbar.exchange.coinbase.rest.api.resource.Resource;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.exchange.coinbase.http.Http;
import com.melonbar.exchange.coinbase.http.HttpClient;
import com.melonbar.exchange.coinbase.model.oracle.OracleRequest;
import com.melonbar.exchange.coinbase.model.response.Response;

public record OracleApiImpl(HttpClient httpClient, Enricher requestEnricher) implements OracleApi {

    @Override
    public Response getOracle(final OracleRequest oracleRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        oracleRequest, Http.GET, Resource.ORACLE))
                .body();
    }
}

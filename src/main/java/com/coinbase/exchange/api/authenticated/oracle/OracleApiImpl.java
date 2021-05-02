package com.coinbase.exchange.api.authenticated.oracle;

import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.enrichment.Enricher;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.http.HttpClient;
import com.coinbase.exchange.model.oracle.OracleRequest;
import com.coinbase.exchange.model.response.Response;

public record OracleApiImpl(HttpClient httpClient, Enricher requestEnricher) implements OracleApi {

    @Override
    public Response getOracle(final OracleRequest oracleRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        oracleRequest, Http.GET, Resource.ORACLE))
                .body();
    }
}

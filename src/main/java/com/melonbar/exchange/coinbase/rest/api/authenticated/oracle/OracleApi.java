package com.melonbar.exchange.coinbase.rest.api.authenticated.oracle;

import com.melonbar.exchange.coinbase.model.oracle.OracleRequest;
import com.melonbar.core.http.response.Response;

public interface OracleApi {

    Response getOracle(final OracleRequest oracleRequest);
}

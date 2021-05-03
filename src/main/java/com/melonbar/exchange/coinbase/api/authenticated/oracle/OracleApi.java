package com.melonbar.exchange.coinbase.api.authenticated.oracle;

import com.melonbar.exchange.coinbase.model.oracle.OracleRequest;
import com.melonbar.exchange.coinbase.model.response.Response;

public interface OracleApi {

    Response getOracle(final OracleRequest oracleRequest);
}

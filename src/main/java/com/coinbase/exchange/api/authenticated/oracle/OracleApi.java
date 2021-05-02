package com.coinbase.exchange.api.authenticated.oracle;

import com.coinbase.exchange.model.oracle.OracleRequest;
import com.coinbase.exchange.model.response.Response;

public interface OracleApi {

    Response getOracle(final OracleRequest oracleRequest);
}

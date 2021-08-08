package com.melonbar.exchange.coinbase.rest.api.authenticated.fills;

import com.melonbar.exchange.coinbase.model.fills.ListFillsRequest;
import com.melonbar.core.http.response.Response;

public interface FillsApi {

    Response listFills(final ListFillsRequest listFillsRequest);
}


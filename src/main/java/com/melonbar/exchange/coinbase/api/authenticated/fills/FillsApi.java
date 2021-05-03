package com.melonbar.exchange.coinbase.api.authenticated.fills;

import com.melonbar.exchange.coinbase.model.fills.ListFillsRequest;
import com.melonbar.exchange.coinbase.model.response.Response;

public interface FillsApi {

    Response listFills(final ListFillsRequest listFillsRequest);
}


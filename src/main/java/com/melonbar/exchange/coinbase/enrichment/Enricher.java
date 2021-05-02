package com.melonbar.exchange.coinbase.enrichment;

import com.melonbar.exchange.coinbase.api.resource.Resource;
import com.melonbar.exchange.coinbase.http.Http;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;

public interface Enricher {

    <T extends BaseRequest> T enrichRequest(final T request, final Http method, final Resource resource);
}

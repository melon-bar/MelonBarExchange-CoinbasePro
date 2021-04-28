package com.coinbase.exchange.enrichment;

import com.coinbase.exchange.api.resource.Resource;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.model.request.BaseRequest;

public interface Enricher {

    <T extends BaseRequest> T enrichRequest(final T request, final Http method, final Resource resource);
}

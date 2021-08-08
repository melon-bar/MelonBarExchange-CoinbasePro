package com.melonbar.exchange.coinbase.enrichment;

import com.melonbar.exchange.coinbase.rest.api.resource.Resource;
import com.melonbar.core.http.Http;
import com.melonbar.core.http.request.BaseRequest;

public interface Enricher {

    <T extends BaseRequest> T enrichRequest(final T request, final Http method, final Resource resource);
}

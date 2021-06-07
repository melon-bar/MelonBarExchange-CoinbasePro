package com.melonbar.exchange.coinbase.model.request;

import com.melonbar.exchange.coinbase.http.Http;
import com.melonbar.exchange.coinbase.util.AppConfig;

public class DebugRequest extends BaseRequest {

    public DebugRequest(final String body, final String requestPath, final Http method) {
        setMethod(method);
        setRequestPath(requestPath);
        setUri(AppConfig.COINBASE_PRO_API_ENDPOINT + requestPath);
        setBody(body);
    }
}

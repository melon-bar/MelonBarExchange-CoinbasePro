package com.melonbar.exchange.coinbase.model.account;

import com.melonbar.exchange.coinbase.annotation.RequestField;
import com.melonbar.core.http.request.BaseRequest;
import lombok.Builder;

/**
 * Requests for Coinbase Pro accounts API.
 */
@Builder
public class AccountsRequest extends BaseRequest {

    /**
     * Account ID.
     */
    @RequestField(index = 0)
    private final String accountId;
}

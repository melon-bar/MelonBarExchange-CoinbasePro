package com.coinbase.exchange.model.account;

import com.coinbase.exchange.annotation.RequestField;
import com.coinbase.exchange.model.request.BaseRequest;
import lombok.Builder;
import lombok.Getter;

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

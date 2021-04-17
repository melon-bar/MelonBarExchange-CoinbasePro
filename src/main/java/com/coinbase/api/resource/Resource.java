package com.coinbase.api.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Resource {

    /**
     * Accounts API resources.
     */
    LIST_ACCOUNTS           ("/accounts"),
    GET_ACCOUNT             ("/accounts/{id}"),
    GET_ACCOUNT_HISTORY     ("/accounts/{id}/ledger"),
    GET_HOLDS               ("/accounts/{id}/ledger");

    /**
     * Orders API resources.
     */

    /**
     * TODO API resources
     */

    // resource uri format
    @Getter private final String uri;
}

package com.melonbar.exchange.coinbase.rest.api.authenticated.accounts;

import com.melonbar.exchange.coinbase.model.response.Response;
import com.melonbar.exchange.coinbase.model.account.AccountsRequest;

public interface AccountsApi {

    Response listAccounts(final AccountsRequest accountsRequest);

    Response getAccount(final AccountsRequest accountsRequest);

    Response getAccountHistory(final AccountsRequest accountsRequest);

    Response getHolds(final AccountsRequest accountsRequest);
}

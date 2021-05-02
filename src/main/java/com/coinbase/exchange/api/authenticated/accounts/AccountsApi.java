package com.coinbase.exchange.api.authenticated.accounts;

import com.coinbase.exchange.model.response.Response;
import com.coinbase.exchange.model.account.AccountsRequest;

public interface AccountsApi {

    Response listAccounts(final AccountsRequest accountsRequest);

    Response getAccount(final AccountsRequest accountsRequest);

    Response getAccountHistory(final AccountsRequest accountsRequest);

    Response getHolds(final AccountsRequest accountsRequest);
}

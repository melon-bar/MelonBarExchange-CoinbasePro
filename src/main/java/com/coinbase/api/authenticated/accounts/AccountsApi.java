package com.coinbase.api.authenticated.accounts;

import com.coinbase.model.Response;
import com.coinbase.model.account.AccountsRequest;

public interface AccountsApi {

    Response listAccounts(final AccountsRequest accountsRequest);

    Response getAccount(final AccountsRequest accountsRequest);

    Response getAccountHistory(final AccountsRequest accountsRequest);

    Response getHolds(final AccountsRequest accountsRequest);
}

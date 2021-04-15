package com.coinbase.api.accounts;

import com.coinbase.authentication.Authentication;
import com.coinbase.client.http.Http;
import com.coinbase.client.http.HttpClient;
import com.coinbase.model.Response;
import com.coinbase.model.account.AccountsRequest;
import com.coinbase.model.annotation.Request;
import com.coinbase.util.router.Router;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.core.UriBuilder;
import java.net.http.HttpRequest;

// TODO: annotation processing for enriching data before http request
@RequiredArgsConstructor
public class AccountsApiImpl implements AccountsApi {

    private static final String LIST_ACCOUNTS_API_URI       = "/accounts";
    private static final String GET_ACCOUNT_API_URI         = "/accounts/{id}";
    private static final String GET_ACCOUNT_HISTORY_API_URI = "/accounts/{id}/ledger";
    private static final String GET_HOLDS_API_URI           = "/accounts/{id}/holds";

    private final HttpClient httpClient;
    private final Authentication authentication;

    @Override
    @Request(path = LIST_ACCOUNTS_API_URI, type = Http.GET)
    public Response listAccounts(final AccountsRequest accountsRequest) {
        return sendAccountsGetRequest(accountsRequest);
    }

    @Override
    @Request(path = GET_ACCOUNT_API_URI, type = Http.GET)
    public Response getAccount(final AccountsRequest accountsRequest) {
        return sendAccountsGetRequest(accountsRequest);
    }

    @Override
    @Request(path = GET_ACCOUNT_HISTORY_API_URI, type = Http.GET)
    public Response getAccountHistory(final AccountsRequest accountsRequest) {
        return sendAccountsGetRequest(accountsRequest);
    }

    @Override
    @Request(path = GET_HOLDS_API_URI, type = Http.GET)
    public Response getHolds(final AccountsRequest accountsRequest) {
        return sendAccountsGetRequest(accountsRequest);
    }

    private Response sendAccountsGetRequest(@NonNull final AccountsRequest accountsRequest) {
        final HttpRequest httpRequest = authentication.enrichHeaders(
                    HttpRequest.newBuilder(),
                    accountsRequest.getMethod().name(),
                    accountsRequest.getUri(),
                    accountsRequest.getBody())
                .uri(UriBuilder.fromUri(accountsRequest.getUri()).build())
                .GET()
                .build();
        return Router.routeRequest(httpClient, httpRequest).body();
    }
}

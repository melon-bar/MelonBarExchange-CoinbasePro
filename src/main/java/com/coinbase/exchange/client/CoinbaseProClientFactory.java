package com.coinbase.exchange.client;

import com.coinbase.exchange.api.authenticated.accounts.AccountsApiImpl;
import com.coinbase.exchange.api.authenticated.oracle.OracleApiImpl;
import com.coinbase.exchange.api.authenticated.orders.OrdersApiImpl;
import com.coinbase.exchange.authentication.Authentication;
import com.coinbase.exchange.authentication.CoinbaseProAuthentication;
import com.coinbase.exchange.enrichment.Enricher;
import com.coinbase.exchange.enrichment.RequestEnricher;
import com.coinbase.exchange.http.HttpClient;
import com.coinbase.exchange.http.HttpClientImpl;

/**
 * Factory class for providing {@link CoinbaseProClientImpl}.
 */
public final class CoinbaseProClientFactory {

    /**
     * Creates basic Coinbase Pro client using user's API credentials. Provides the most basic barebones configuration
     * of the {@link CoinbaseProClientImpl}.
     *
     * @param apiKey API key
     * @param apiPassword API password
     * @param apiSecretKey API secret key
     * @return {@link CoinbaseProClient}
     */
    public static CoinbaseProClient createClient(final String apiKey,
                                                 final String apiPassword,
                                                 final String apiSecretKey) {
        final Authentication authentication = new CoinbaseProAuthentication(apiKey, apiPassword, apiSecretKey);
        final HttpClient httpClient = new HttpClientImpl(authentication, java.net.http.HttpClient.newHttpClient());
        final Enricher requestEnricher = new RequestEnricher();

        return new CoinbaseProClientImpl(
                new AccountsApiImpl(httpClient, requestEnricher),
                new OrdersApiImpl(httpClient, requestEnricher),
                new OracleApiImpl(httpClient, requestEnricher));
    }
}

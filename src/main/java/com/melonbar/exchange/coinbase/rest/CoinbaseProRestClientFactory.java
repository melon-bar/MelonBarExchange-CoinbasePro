package com.melonbar.exchange.coinbase.rest;

import com.melonbar.exchange.coinbase.rest.api.authenticated.accounts.AccountsApiImpl;
import com.melonbar.exchange.coinbase.rest.api.authenticated.fills.FillsApiImpl;
import com.melonbar.exchange.coinbase.rest.api.authenticated.oracle.OracleApiImpl;
import com.melonbar.exchange.coinbase.rest.api.authenticated.orders.OrdersApiImpl;
import com.melonbar.exchange.coinbase.rest.api.marketdata.MarketDataApiImpl;
import com.melonbar.exchange.coinbase.authentication.Authentication;
import com.melonbar.exchange.coinbase.authentication.CoinbaseProAuthentication;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.exchange.coinbase.enrichment.RequestEnricher;
import com.melonbar.core.http.HttpClient;
import com.melonbar.exchange.coinbase.http.HttpClientImpl;

/**
 * Factory class for providing {@link CoinbaseProRestClientImpl}.
 */
public final class CoinbaseProRestClientFactory {

    /**
     * Creates basic Coinbase Pro client using user's API credentials. Provides the most basic barebones configuration
     * of the {@link CoinbaseProRestClientImpl}.
     *
     * @param apiKey API key
     * @param apiPassword API password
     * @param apiSecretKey API secret key
     * @return {@link CoinbaseProRestClient}
     */
    public static CoinbaseProRestClient createClient(final String apiKey,
                                                     final String apiPassword,
                                                     final String apiSecretKey) {
        return createClient(new CoinbaseProAuthentication(apiKey, apiPassword, apiSecretKey));
    }

    public static CoinbaseProRestClient createClient(final Authentication authentication) {
        final HttpClient httpClient = new HttpClientImpl(authentication, java.net.http.HttpClient.newHttpClient());
        final Enricher requestEnricher = new RequestEnricher();

        return new CoinbaseProRestClientImpl(
                new AccountsApiImpl(httpClient, requestEnricher),
                new OrdersApiImpl(httpClient, requestEnricher),
                new FillsApiImpl(httpClient, requestEnricher),
                new OracleApiImpl(httpClient, requestEnricher),
                new MarketDataApiImpl(httpClient, requestEnricher));
    }

    public static CoinbaseProRestClient createDebugClient(final String apiKey,
                                                          final String apiPassword,
                                                          final String apiSecretKey) {
        final HttpClient httpClient = new HttpClientImpl(
                new CoinbaseProAuthentication(apiKey, apiPassword, apiSecretKey),
                java.net.http.HttpClient.newHttpClient());
        final Enricher requestEnricher = new RequestEnricher();

        return new CoinbaseProDebugRestClient(
                httpClient,
                new AccountsApiImpl(httpClient, requestEnricher),
                new OrdersApiImpl(httpClient, requestEnricher),
                new FillsApiImpl(httpClient, requestEnricher),
                new OracleApiImpl(httpClient, requestEnricher),
                new MarketDataApiImpl(httpClient, requestEnricher));
    }
}

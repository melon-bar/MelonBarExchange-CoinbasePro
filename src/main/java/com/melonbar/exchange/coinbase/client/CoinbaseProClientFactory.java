package com.melonbar.exchange.coinbase.client;

import com.melonbar.exchange.coinbase.api.authenticated.accounts.AccountsApiImpl;
import com.melonbar.exchange.coinbase.api.authenticated.fills.FillsApiImpl;
import com.melonbar.exchange.coinbase.api.authenticated.oracle.OracleApiImpl;
import com.melonbar.exchange.coinbase.api.authenticated.orders.OrdersApiImpl;
import com.melonbar.exchange.coinbase.api.marketdata.MarketDataApiImpl;
import com.melonbar.exchange.coinbase.authentication.Authentication;
import com.melonbar.exchange.coinbase.authentication.CoinbaseProAuthentication;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.exchange.coinbase.enrichment.RequestEnricher;
import com.melonbar.exchange.coinbase.http.HttpClient;
import com.melonbar.exchange.coinbase.http.HttpClientImpl;

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
                new FillsApiImpl(httpClient, requestEnricher),
                new OracleApiImpl(httpClient, requestEnricher),
                new MarketDataApiImpl(httpClient, requestEnricher));
    }
}

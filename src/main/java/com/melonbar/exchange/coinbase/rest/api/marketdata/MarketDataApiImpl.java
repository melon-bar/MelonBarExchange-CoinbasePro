package com.melonbar.exchange.coinbase.rest.api.marketdata;

import com.melonbar.exchange.coinbase.rest.api.resource.Resource;
import com.melonbar.exchange.coinbase.enrichment.Enricher;
import com.melonbar.exchange.coinbase.http.Http;
import com.melonbar.exchange.coinbase.http.HttpClient;
import com.melonbar.exchange.coinbase.model.currencies.CurrenciesRequest;
import com.melonbar.exchange.coinbase.model.currencies.CurrencyByIdRequest;
import com.melonbar.exchange.coinbase.model.products.ProductByIdRequest;
import com.melonbar.exchange.coinbase.model.products.ProductCandlesRequest;
import com.melonbar.exchange.coinbase.model.products.ProductOrderBookRequest;
import com.melonbar.exchange.coinbase.model.products.ProductRequest;
import com.melonbar.exchange.coinbase.model.response.Response;

public record MarketDataApiImpl(HttpClient httpClient, Enricher requestEnricher) implements MarketDataApi {

    @Override
    public Response getCurrencies(final CurrenciesRequest currenciesRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        currenciesRequest, Http.GET, Resource.CURRENCY))
                .body();
    }

    @Override
    public Response getCurrencyById(final CurrencyByIdRequest currencyByIdRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        currencyByIdRequest, Http.GET, Resource.CURRENCY_BY_ID))
                .body();
    }

    @Override
    public Response getProducts(final ProductRequest productRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        productRequest, Http.GET, Resource.PRODUCT))
                .body();
    }

    @Override
    public Response getProductsById(final ProductByIdRequest productByIdRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        productByIdRequest, Http.GET, Resource.PRODUCT_BY_ID))
                .body();
    }

    @Override
    public Response getProductOrderBook(final ProductOrderBookRequest productOrderBookRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        productOrderBookRequest, Http.GET, Resource.PRODUCT_ORDER_BOOK))
                .body();
    }

    @Override
    public Response getProductTicker(final ProductByIdRequest productByIdRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        productByIdRequest, Http.GET, Resource.PRODUCT_TICKER))
                .body();
    }

    @Override
    public Response getProductTrades(final ProductByIdRequest productByIdRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        productByIdRequest, Http.GET, Resource.PRODUCT_TRADES))
                .body();
    }

    @Override
    public Response getProductCandles(final ProductCandlesRequest productCandlesRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        productCandlesRequest, Http.GET, Resource.PRODUCT_CANDLES))
                .body();
    }

    @Override
    public Response getProduct24HourStats(final ProductByIdRequest productByIdRequest) {
        return httpClient.send(
                requestEnricher.enrichRequest(
                        productByIdRequest, Http.GET, Resource.PRODUCT_STATS))
                .body();
    }
}

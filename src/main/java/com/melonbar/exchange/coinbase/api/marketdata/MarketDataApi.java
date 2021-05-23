package com.melonbar.exchange.coinbase.api.marketdata;

import com.melonbar.exchange.coinbase.model.currencies.CurrenciesRequest;
import com.melonbar.exchange.coinbase.model.currencies.CurrencyByIdRequest;
import com.melonbar.exchange.coinbase.model.products.ProductByIdRequest;
import com.melonbar.exchange.coinbase.model.products.ProductCandlesRequest;
import com.melonbar.exchange.coinbase.model.products.ProductOrderBookRequest;
import com.melonbar.exchange.coinbase.model.products.ProductRequest;
import com.melonbar.exchange.coinbase.model.response.Response;

public interface MarketDataApi {

    Response getCurrencies(final CurrenciesRequest currenciesRequest);

    Response getCurrencyById(final CurrencyByIdRequest currencyByIdRequest);

    Response getProducts(final ProductRequest productRequest);

    Response getProductsById(final ProductByIdRequest productByIdRequest);

    Response getProductOrderBook(final ProductOrderBookRequest productOrderBookRequest);

    Response getProductTicker(final ProductByIdRequest productByIdRequest);

    Response getProductTrades(final ProductByIdRequest productByIdRequest);

    Response getProductCandles(final ProductCandlesRequest productCandlesRequest);

    Response getProduct24HourStats(final ProductByIdRequest productByIdRequest);
}

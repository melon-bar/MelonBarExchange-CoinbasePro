package com.melonbar.exchange.coinbase.util;

import com.melonbar.core.util.Guard;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public final class AppConfig {

    private static final String APP_CONFIG_FILE_NAME = "application.properties";
    private static final Properties PROPERTIES = new Properties();

    public static final String COINBASE_PRO_API_ENDPOINT;
    public static final String COINBASE_PRO_WEBHOOK_FEED_ENDPOINT;

    static {
        try (final InputStream propertiesStream
                     = AppConfig.class.getClassLoader().getResourceAsStream(APP_CONFIG_FILE_NAME)){
            PROPERTIES.load(propertiesStream);
        } catch (IOException ioException) {
            log.error("Could not load {} due to IOException.", APP_CONFIG_FILE_NAME, ioException);
        }

        // load basic endpoint values for Coinbase Pro
        COINBASE_PRO_API_ENDPOINT = getValue("coinbase_pro_authenticated_endpoint");
        COINBASE_PRO_WEBHOOK_FEED_ENDPOINT = getValue("coinbase_pro_websocket_feed_endpoint");
    }

    public static String getValue(final String key) {
        Guard.nonNull(key);
        synchronized (PROPERTIES) {
            return PROPERTIES.getProperty(key);
        }
    }
}

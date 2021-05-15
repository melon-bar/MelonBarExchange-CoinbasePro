package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.util.AppConfig;
import com.melonbar.exchange.coinbase.websocket.message.SubscribeMessage;
import com.melonbar.exchange.coinbase.websocket.message.model.Channel;
import com.melonbar.exchange.coinbase.websocket.processing.StringMessageHandler;

import javax.websocket.ClientEndpoint;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Session-based websocket feed client for Coinbase Pro.
 *
 * @see <a href=https://docs.pro.coinbase.com/#websocket-feed>Coinbase Pro websocket feed documentation</a>
 */
@ClientEndpoint
public class CoinbaseProWebsocketFeedClient extends ReactiveWebsocketFeedClient {

    /**
     * Internal constructor that initializes the {@link javax.websocket.Session} using the websocket feed endpoint
     * provided by {@link AppConfig}.
     */
    protected CoinbaseProWebsocketFeedClient() {
        super(AppConfig.COINBASE_PRO_WEBHOOK_FEED_ENDPOINT);
    }

    /**
     * Initializes a Coinbase Pro websocket feed {@link Builder client builder} using a new instance.
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder(new CoinbaseProWebsocketFeedClient());
    }

    /**
     * Add {@link StringMessageHandler}s to the member {@lnk AggregatedMessageHandler}.
     *
     * @param messageHandlers {@link StringMessageHandler Handler(s)} to add
     */
    public void addMessageHandlers(final StringMessageHandler ... messageHandlers) {
        for (final StringMessageHandler messageHandler : messageHandlers) {
            getAggregatedMessageHandler().addMessageHandler(messageHandler);
        }
    }

    /**
     * Internal static builder class for convenient and controlled initialization of
     * {@link CoinbaseProWebsocketFeedClient}.
     */
    public static class Builder {
        private final CoinbaseProWebsocketFeedClient coinbaseProWebsocketFeedClient;
        private final List<Channel> channels = new LinkedList<>();
        private final List<ProductId> productIds = new LinkedList<>();

        /**
         * Internal constructor for instantiating {@link Builder}.
         *
         * @param coinbaseProWebsocketFeedClient {@link CoinbaseProWebsocketFeedClient} to build
         */
        protected Builder(final CoinbaseProWebsocketFeedClient coinbaseProWebsocketFeedClient) {
            this.coinbaseProWebsocketFeedClient = coinbaseProWebsocketFeedClient;
        }

        /**
         * Wither for {@link StringMessageHandler}s.
         *
         * @param messageHandlers {@link StringMessageHandler}s
         * @return {@link Builder}
         */
        public Builder withMessageHandlers(final StringMessageHandler ... messageHandlers) {
            coinbaseProWebsocketFeedClient.addMessageHandlers(messageHandlers);
            return this;
        }

        /**
         * Wither for {@link Channel}s.
         *
         * @param channels {@link Channel}s
         * @return {@link Builder}
         */
        public Builder withChannels(final Channel ... channels) {
            this.channels.addAll(Arrays.asList(channels));
            return this;
        }

        /**
         * Wither for {@link ProductId}s.
         *
         * @param productIds {@link ProductId}s
         * @return {@link Builder}
         */
        public Builder withProducts(final ProductId ... productIds) {
            this.productIds.addAll(Arrays.asList(productIds));
            return this;
        }

        /**
         * Finalizes initialization of {@link CoinbaseProWebsocketFeedClient} by creating the final 
         * {@link SubscribeMessage} based on the inputs from {@link #withChannels(Channel...)} and
         * {@link #withProducts(ProductId...)}.
         * 
         * @return Active {@link CoinbaseProWebsocketFeedClient}
         */
        public CoinbaseProWebsocketFeedClient build() {
            coinbaseProWebsocketFeedClient.sendMessage(
                    SubscribeMessage.builder()
                            .channels(channels.toArray(new Channel[0]))
                            .productIds(productIds.toArray(new ProductId[0]))
                            .build());

            return coinbaseProWebsocketFeedClient;
        }
    }
}

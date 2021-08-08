package com.melonbar.exchange.coinbase.websocket;

import com.melonbar.core.model.ProductId;
import com.melonbar.exchange.coinbase.util.AppConfig;
import com.melonbar.exchange.coinbase.websocket.message.SubscribeMessage;
import com.melonbar.exchange.coinbase.websocket.message.model.Channel;
import com.melonbar.exchange.coinbase.websocket.processing.AggregatedMessageHandler;
import com.melonbar.exchange.coinbase.websocket.processing.tracking.Tracker;

import javax.websocket.ClientEndpoint;
import javax.websocket.MessageHandler;
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
     * Internal constructor that invokes super constructor.
     *
     * @see ReactiveWebsocketFeedClient
     */
    protected CoinbaseProWebsocketFeedClient() {
        super();
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
     * Add {@link MessageHandler.Whole}s to the member {@link AggregatedMessageHandler}.
     *
     * @param messageHandlers {@link MessageHandler.Whole Handler(s)} to add
     */
    @SafeVarargs
    public final void addMessageHandlers(final MessageHandler.Whole<String>... messageHandlers) {
        for (final MessageHandler.Whole<String> messageHandler : messageHandlers) {
            getAggregatedMessageHandler().addMessageHandler(messageHandler);
        }
    }

    /**
     * Register the input {@link Tracker}. The new {@link MessageHandler.Whole} is instantiated with
     * {@link Tracker#update}.
     *
     * @param tracker Tracker to register
     * @param <T> Implementation of {@link Tracker}
     */
    public <T extends Tracker<String>> void addTracker(final T tracker) {
        addMessageHandler(tracker::update);
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
         * Wither for {@link MessageHandler.Whole}s.
         *
         * @param messageHandlers {@link MessageHandler.Whole}s
         * @return {@link Builder}
         */
        @SafeVarargs
        public final Builder withMessageHandlers(final MessageHandler.Whole<String>... messageHandlers) {
            coinbaseProWebsocketFeedClient.addMessageHandlers(messageHandlers);
            return this;
        }

        /**
         * Wither for registering {@link Tracker}. Registers {@link Tracker#update} as a {@link MessageHandler.Whole}.
         * The same may be accomplished adding {@link Tracker#update} using {@link #withMessageHandlers}.
         *
         * @param trackers {@link Tracker}s
         * @return {@link Builder}
         */
        @SafeVarargs
        public final Builder withTrackers(final Tracker<String> ... trackers) {
            Arrays.stream(trackers).forEach(coinbaseProWebsocketFeedClient::addTracker);
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
         * Wither for text buffer size in bytes.
         *
         * @param bufferSize Size in bytes
         * @return {@link CoinbaseProWebsocketFeedClient.Builder}
         */
        public Builder withTextBufferSize(final int bufferSize) {
            coinbaseProWebsocketFeedClient.setBufferSize(bufferSize);
            return this;
        }

        /**
         * Finalizes initialization of {@link CoinbaseProWebsocketFeedClient} by creating the final 
         * {@link SubscribeMessage} based on the inputs from {@link #withChannels(Channel...)} and
         * {@link #withProducts(ProductId...)}.
         *
         * <p> Establishes actual TCP connection with the websocket endpoint provided by {@link AppConfig}
         * for Coinbase Pro. Once connection is established, the initiating {@link SubscribeMessage} is sent.
         * 
         * @return Active {@link CoinbaseProWebsocketFeedClient}
         */
        public CoinbaseProWebsocketFeedClient build() {
            // init TCP connection
            coinbaseProWebsocketFeedClient.open(AppConfig.COINBASE_PRO_WEBHOOK_FEED_ENDPOINT);

            // send subscription messages
            coinbaseProWebsocketFeedClient.sendMessage(
                    SubscribeMessage.builder()
                            .channels(channels.toArray(new Channel[0]))
                            .productIds(productIds.toArray(new ProductId[0]))
                            .build());

            return coinbaseProWebsocketFeedClient;
        }
    }
}

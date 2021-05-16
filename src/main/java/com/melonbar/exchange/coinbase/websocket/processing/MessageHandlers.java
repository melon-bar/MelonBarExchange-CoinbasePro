package com.melonbar.exchange.coinbase.websocket.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melonbar.exchange.coinbase.util.Guard;
import com.melonbar.exchange.coinbase.util.JsonUtils;
import com.melonbar.exchange.coinbase.websocket.message.FeedMessage;
import com.melonbar.exchange.coinbase.websocket.processing.predicated.PredicatedMessageHandler;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.MessageHandler;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Catalog of common helper functions and {@link MessageHandler} generators for general use cases.
 */
@Slf4j
public final class MessageHandlers {

    private static final String TICKER_PRICE = "price";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Pretty prints input message. Mostly for debugging purposes.
     *
     * @param message Inbound message
     */
    public static void prettyPrint(final String message) {
        try {
            log.info("{}", OBJECT_MAPPER.reader()
                    .readTree(message).toPrettyString());
        } catch (Exception exception) {
            log.info("Could not pretty-print: {}", message);
        }
    }

    /**
     * Function that accepts ticker json message and extracts the current price.
     *
     * @return {@link StringMessageFunction} for extracting price from inbound ticker
     */
    public static StringMessageFunction<BigDecimal> getPriceFromTicker() {
        return (message) -> new BigDecimal(
                Objects.requireNonNull(
                        JsonUtils.extractField(TICKER_PRICE, message)));
    }

    /**
     * Factory method for creating {@link PredicatedMessageHandler} provided an input {@link Predicate} on
     * {@link T} and the base {@link MessageHandler.Whole}.
     *
     * @param messageHandler Base handler whose {@link MessageHandler.Whole#onMessage} will be invoked
     * @param predicate {@link Predicate} guarding invocation
     * @param <T> Message type
     * @return {@link PredicatedMessageHandler}
     */
    public static <T> PredicatedMessageHandler<T> predicated(final MessageHandler.Whole<T> messageHandler,
                                                             final Predicate<T> predicate) {
        return new PredicatedMessageHandler<T>(predicate, messageHandler);
    }

    /**
     * Aggregates {@link StringMessageHandler} by the json message type, defined by its <code>type</code> field.
     * Each {@link StringMessageHandler} is converted into a {@link PredicatedMessageHandler} by applying a new
     * {@link Predicate} that checks equality between input <code>type</code> and the corresponding key value
     * from an inbound json message. If the type matches, then the {@link AggregatedMessageHandler} is invoked.
     *
     * @param type Expected type
     * @param messageHandlers {@link StringMessageHandler}s to aggregate into {@link AggregatedMessageHandler}
     * @return {@link PredicatedMessageHandler}
     */
    public static PredicatedMessageHandler<String> byType(final String type,
                                                          final StringMessageHandler ... messageHandlers) {
        Guard.nonNull(type, messageHandlers);
        final AggregatedMessageHandler<String> aggregate = AggregatedMessageHandler.create();
        Arrays.stream(messageHandlers)
                .forEach(aggregate::addMessageHandler);
        // predicate aggregation of input message handlers with type field check
        return predicated(aggregate,
                (message) -> type.equals(JsonUtils.extractField(FeedMessage.TYPE_FIELD, message)));
    }
}

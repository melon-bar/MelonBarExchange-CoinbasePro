package com.melonbar.exchange.coinbase.websocket.processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melonbar.core.util.Guard;
import com.melonbar.core.util.JsonUtils;
import com.melonbar.exchange.coinbase.websocket.message.FeedMessage;
import com.melonbar.exchange.coinbase.websocket.processing.predicated.PredicatedMessageHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.MessageHandler;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
     * Creates a new {@link TimedMessageHandler}. Effectively acts as a timed wrapper for other
     * {@link MessageHandler.Whole} implementations.
     *
     * @param messageHandler {@link MessageHandler.Whole} which will be invoked by {@link TimedMessageHandler}
     * @param delay Delay in milliseconds
     * @param <T> Message type
     * @return {@link TimedMessageHandler}
     */
    public static <T> TimedMessageHandler<T> timed(final MessageHandler.Whole<T> messageHandler, final long delay) {
        return new TimedMessageHandler<>(messageHandler, delay);
    }

    /**
     * Factory method for creating {@link PredicatedMessageHandler} provided an input {@link Predicate} on
     * {@link T} and the base {@link MessageHandler.Whole}.
     *
     * @param <T> Message type
     * @param predicate {@link Predicate} guarding invocation
     * @param messageHandler Base handler whose {@link MessageHandler.Whole#onMessage} will be invoked
     * @return {@link PredicatedMessageHandler}
     */
    public static <T> PredicatedMessageHandler<T> predicated(final Predicate<T> predicate,
                                                             final MessageHandler.Whole<T> messageHandler) {
        return new PredicatedMessageHandler<T>(predicate, messageHandler);
    }

    /**
     * Converts varargs of {@link MessageHandler.Whole} into a single {@link AggregatedMessageHandler}. Does not
     * accept null input or arg size < 2, as a an aggregation of 1 {@link MessageHandler.Whole} does not make sense.
     *
     * @param messageHandlers {@link MessageHandler.Whole}
     * @param <T> Message type
     * @return {@link AggregatedMessageHandler}
     * @throws IllegalArgumentException When no args provided or only 1 arg
     */
    @SafeVarargs
    public static <T> AggregatedMessageHandler<T> aggregate(final MessageHandler.Whole<T>... messageHandlers) {
        if (messageHandlers == null || messageHandlers.length < 2) {
            throw new IllegalArgumentException("Aggregation attempt on null input or a single message handler.");
        }
        final AggregatedMessageHandler<T> aggregate = AggregatedMessageHandler.create();
        Arrays.stream(messageHandlers)
                .forEach(aggregate::addMessageHandler);
        return aggregate;
    }

    /**
     * Aggregates {@link StringMessageHandler} by the json message type, defined by its <code>type</code> field.
     * Each {@link StringMessageHandler} is converted into a {@link PredicatedMessageHandler} by applying a new
     * {@link Predicate} that checks equality between input <code>type</code> and the corresponding key value
     * from an inbound json message. If the type matches, then the {@link AggregatedMessageHandler} is invoked.
     *
     * <p> If only one handler is inputted, no aggregation occurs and <code>messageHandlers[0]</code> is directly
     * used.
     *
     * @param type Expected type
     * @param messageHandlers {@link StringMessageHandler}s to aggregate into {@link AggregatedMessageHandler}
     * @return {@link PredicatedMessageHandler}
     */
    @SafeVarargs
    public static PredicatedMessageHandler<String> byType(final String type,
                                                          final MessageHandler.Whole<String> ... messageHandlers) {
        Guard.nonNull(type, messageHandlers);

        // only aggregate the message handlers if more than one is inputted
        final MessageHandler.Whole<String> messageHandler = messageHandlers.length > 1
                ? aggregate(messageHandlers)
                : messageHandlers[0];

        // predicate aggregation of input message handlers with type field check
        return predicated((message) -> type.equals(
                    JsonUtils.extractField(FeedMessage.TYPE_FIELD, message)),
                messageHandler);
    }

    /**
     * Returns special {@link Builder} for varargs input for {@link MessageHandler.Whole}. Final built result
     * is an array of {@link MessageHandler.Whole}.
     *
     * @return {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Simple static builder class for creating arrays of {@link MessageHandler.Whole} for concise vararg input
     * for simple aggregation.
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Builder {

        private final List<MessageHandler.Whole<String>> messageHandlers = new LinkedList<>();

        public Builder byType(final String type, final StringMessageHandler... messageHandlers) {
            this.messageHandlers.add(MessageHandlers.byType(type, messageHandlers));
            return this;
        }

        public Builder aggregate(final StringMessageHandler... messageHandlers) {
            this.messageHandlers.add(MessageHandlers.aggregate(messageHandlers));
            return this;
        }

        public Builder predicated(final Predicate<String> predicate,
                                  final StringMessageHandler messageHandler) {
            this.messageHandlers.add(MessageHandlers.predicated(predicate, messageHandler));
            return this;
        }

        @SuppressWarnings("unchecked")
        public MessageHandler.Whole<String>[] build() {
            return (MessageHandler.Whole<String>[]) messageHandlers.toArray();
        }
    }
}

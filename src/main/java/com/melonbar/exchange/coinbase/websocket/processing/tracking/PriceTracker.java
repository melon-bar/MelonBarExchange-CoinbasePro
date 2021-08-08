package com.melonbar.exchange.coinbase.websocket.processing.tracking;

import com.melonbar.core.model.ProductId;
import com.melonbar.core.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Uses inbound ticker messages to track the prices of various products in quote currency. May be used as a
 * singleton authority on quote currency prices provided a {@link ProductId}. All updates and accesses are
 * thread-safe.
 *
 * <p> The available {@link ProductId}s for tracking are dependent on the present product types from inbound
 * ticker messages. Real-time accurate tracking relies on active websocket feed connection(s). This tracker will
 * store the last received valid price update, meaning prices may be accessed after a connection is terminated.
 *
 * <p> TODO: Timestamp tracking by product on the last received update to allow for stale price detection.
 */
@Slf4j
@Getter
public class PriceTracker implements Tracker<String> {

    private static final String SEQUENCE_FIELD = "sequence";
    private static final String PRICE_FIELD = "price";
    private static final String PRODUCT_ID_FIELD = "product_id";
    private static final PriceRecord NO_VALUE = new PriceRecord(BigDecimal.valueOf(-1), -1);

    private final Map<String, PriceRecord> priceRecords = new ConcurrentHashMap<>();

    /**
     * Upon receiving a message, first extract the sequence number. A sequence number is valid if the last stored
     * sequence for a particular product is smaller. State update is thread-safe and effectively a no-op when the
     * product ID or sequence ID is invalid, or during any {@link Exception}.
     *
     * <p> Note that the product IDs are stored as strings as opposed to instances of {@link ProductId}.
     *
     * @param message Inbound websocket message
     */
    public void update(final String message) {
        try {
            final String productId = getProductId(message);
            final long sequence = getSequence(message);
            /*
             * May update the price record for a given valid product ID provided the sequence number
             * is greater than the previously stored sequence number for the product ID.
             */
            if (productId != null && isValidSequence(priceRecords.get(productId), sequence)) {
                priceRecords.put(productId,
                        new PriceRecord(getPrice(message), sequence));
            }
        } catch (Exception exception) {
            log.warn("Got exception during price tracking update, message: [{}]", message, exception);
        }
    }

    /**
     * Get the latest-recorded price for the input {@link ProductId}.
     *
     * @param productId {@link ProductId}
     * @return Current price if present, otherwise <code>BigDecimal.valueOf(-1)</code>
     */
    public BigDecimal getPrice(final ProductId productId) {
        return priceRecords.getOrDefault(productId.toString(), NO_VALUE).price;
    }

    /**
     * Returns the set of currently-tracked product IDs.
     *
     * @return Set of tracked product IDs
     */
    public Set<String> getTrackedProductIds() {
        return priceRecords.keySet();
    }

    /**
     * Determines if the input sequence qualifies the inbound message for price update when compared with the
     * target {@link PriceRecord}.
     *
     * @param record Current record
     * @param sequence Inbound sequence number
     * @return True if sequence qualifies inbound message, false otherwise
     */
    private boolean isValidSequence(final PriceRecord record, final long sequence) {
        return record == null || record.compareTo(sequence) < 0;
    }

    /**
     * Extract product ID value from json message
     *
     * @param message Json message
     * @return Product ID as {@link String}
     */
    private String getProductId(final String message) {
        return JsonUtils.extractField(PRODUCT_ID_FIELD, message);
    }

    /**
     * Extract price value from json message.
     *
     * @param message Json message
     * @return Price as {@link BigDecimal}
     * @throws NullPointerException When price extraction failed to panic out of state update
     */
    private BigDecimal getPrice(final String message) {
        return new BigDecimal(
                // should avoid polluting the last good known price, so throw exception during failure
                Objects.requireNonNull(
                        JsonUtils.extractField(PRICE_FIELD, message)));
    }

    /**
     * Extract sequence number from json message.
     *
     * @param message Json message
     * @return Sequence number
     */
    private long getSequence(final String message) {
        return Long.parseLong(
                JsonUtils.extractField(SEQUENCE_FIELD, message, "-1"));
    }

    /**
     * Immutable internal data structure that is comparable to {@link Long}, for sequence number comparison. Stores
     * the last known price and corresponding sequence number.
     */
    @AllArgsConstructor
    private static class PriceRecord implements Comparable<Long> {

        protected BigDecimal price;
        protected long sequence;

        @Override
        public int compareTo(Long other) {
            return Long.compare(sequence, other);
        }
    }
}

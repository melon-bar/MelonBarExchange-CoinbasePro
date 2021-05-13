package com.melonbar.exchange.coinbase.testutil;

import com.melonbar.exchange.coinbase.model.core.Currency;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import com.melonbar.exchange.coinbase.websocket.message.model.Channel;
import com.melonbar.exchange.coinbase.websocket.message.model.L2OrderTuple;
import org.joda.time.DateTime;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.UUID;

public final class RandomValueProvider {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static Object random(final Class<?> type) {
        if (type == Boolean.class) {
            return RANDOM.nextBoolean();
        }
        if (type == Integer.class) {
            return RANDOM.nextInt();
        }
        if (type == Long.class) {
            return RANDOM.nextLong();
        }
        if (type == Double.class) {
            return RANDOM.nextDouble();
        }
        if (type == BigDecimal.class) {
            return BigDecimal.valueOf(RANDOM.nextLong());
        }
        if (type == String.class) {
            return new UUID(RANDOM.nextLong(), RANDOM.nextLong()).toString();
        }
        if (type == DateTime.class) {
            return DateTime.now();
        }
        if (type.isEnum()) {
            return randomEnum(type);
        }
        if (type == ProductId.class) {
            return new ProductId(
                    randomEnum(Currency.Unit.class),
                    randomEnum(Currency.Unit.class));
        }
        if (type == L2OrderTuple.class) {
            return L2OrderTuple.builder()
                    .side(RANDOM.nextBoolean() ? randomEnum(OrderSide.class) : null)
                    .price((BigDecimal)random(BigDecimal.class))
                    .size((BigDecimal)random(BigDecimal.class))
                    .build();
        }
        if (type == Channel.class) {
            return new Channel(
                    randomEnum(Channel.ChannelType.class), randomArray(ProductId.class, 5));
        }
        if (type.isArray()) {
            return randomArray(type.getComponentType(), 10);
        }
        throw new IllegalArgumentException(
                "Provided class type not supported for random value generation: " + type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] randomArray(final Class<T> type, final int n) {
        final T[] arr = (T[]) Array.newInstance(type, n);
        for (int i = 0; i < n; i++) {
            arr[i] = (T) random(type);
        }
        return arr;
    }

    private static <T> T randomEnum(final Class<T> enumType) {
        return enumType.getEnumConstants()[RANDOM.nextInt(enumType.getEnumConstants().length)];
    }
}

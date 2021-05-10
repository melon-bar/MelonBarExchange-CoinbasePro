package com.melonbar.exchange.coinbase.websocket.message.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import com.melonbar.exchange.coinbase.util.Guard;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Arrays;

@Builder
public class L2OrderTuple {

    public static final String DELIMITER = ",";

    private final OrderSide orderSide;
    private final BigDecimal price;
    private final BigDecimal size;

    @JsonValue
    public String[] toJson() {
        Guard.nonNull(price, size);
        return orderSide == null
                ? new String[]{price.toString(), size.toString()}
                : new String[]{orderSide.toString(), price.toString(), size.toString()};
    }

    public static L2OrderTuple valueOf(final String[] args) {
        return switch (args.length) {
            case 2 -> L2OrderTuple.builder()
                    .price(new BigDecimal(args[0]))
                    .size(new BigDecimal(args[1]))
                    .build();
            case 3 -> L2OrderTuple.builder()
                    .orderSide(OrderSide.valueOf(args[0].toUpperCase()))
                    .price(new BigDecimal(args[1]))
                    .size(new BigDecimal(args[2]))
                    .build();
            default -> throw new IllegalArgumentException(
                    "Unexpected number of elements: " + Arrays.toString(args));
        };
    }
}

package com.melonbar.exchange.coinbase.websocket.message.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.melonbar.core.util.Guard;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Arrays;

@Builder
public class L2OrderTuple {

    public static final String DELIMITER = ",";

    private final OrderSide side;
    private final BigDecimal price;
    private final BigDecimal size;

    @JsonValue
    public String[] toJson() {
        Guard.nonNull(price, size);
        return side == null
                ? new String[]{price.toString(), size.toString()}
                : new String[]{side.toString(), price.toString(), size.toString()};
    }

    public static L2OrderTuple valueOf(final String[] args) {
        return switch (args.length) {
            case 2 -> L2OrderTuple.builder()
                    .price(new BigDecimal(args[0]))
                    .size(new BigDecimal(args[1]))
                    .build();
            case 3 -> L2OrderTuple.builder()
                    .side(OrderSide.valueOf(args[0].toUpperCase()))
                    .price(new BigDecimal(args[1]))
                    .size(new BigDecimal(args[2]))
                    .build();
            default -> throw new IllegalArgumentException(
                    "Unexpected number of elements: " + Arrays.toString(args));
        };
    }
}

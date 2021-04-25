package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.BodyField;
import com.coinbase.exchange.model.order.flag.TimeInForce;

import lombok.Builder;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Builder
public class LimitOrderRequest extends BaseNewOrderRequest {

    @BodyField(key = "price", required = true)
    private final BigDecimal cryptoPrice;

    @BodyField(key = "size", required = true)
    private final BigDecimal orderSize;

    @BodyField(key = "time_in_force")
    private final TimeInForce timeInForce;

    @BodyField(key = "cancel_after")
    private final DateTime cancelAfter;

    @BodyField(key = "post_only")
    private final Boolean postOnly;

    /**
     * Validates that when these fields are present:
     * <p>
     * 1. <code>cancelAfter</code>: <code>timeInForce</code> is GTT
     * <p>
     * 2. <code>postOnly</code>: <code>timeInForce</code> is not IOC or FOK
     *
     * @return True if base order constraints and limit order constraints are met, otherwise false.
     */
    @Override
    public boolean validateRequest() {
        return super.validateBaseRequest()
                && (ifPresent(timeInForce, TimeInForce.GTT::equals)
                    || ifPresent(postOnly, p -> !(TimeInForce.IOC == timeInForce || TimeInForce.FOK == timeInForce)));
    }
}

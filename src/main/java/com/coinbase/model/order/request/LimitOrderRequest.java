package com.coinbase.model.order.request;

import com.coinbase.model.annotation.Required;
import com.coinbase.model.order.flag.TimeInForce;

import lombok.Builder;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Builder
public class LimitOrderRequest extends BaseNewOrderRequest {

    @Required
    private final BigDecimal cryptoPrice;

    @Required
    private final BigDecimal orderSize;

    private TimeInForce timeInForce;
    private DateTime cancelAfter;
    private Boolean postOnly;

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

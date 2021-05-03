package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.model.core.Product;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import com.melonbar.exchange.coinbase.model.order.flag.OrderStop;
import com.melonbar.exchange.coinbase.model.order.flag.OrderType;
import com.melonbar.exchange.coinbase.model.order.flag.SelfTradePrevention;
import com.melonbar.exchange.coinbase.model.order.flag.TimeInForce;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import lombok.Builder;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public class LimitOrderRequest extends BaseRequest {

    @BodyField(key = "side", required = true)
    private final OrderSide orderSide;

    @BodyField(key = "product_id", required = true)
    private final Product product;

    @BodyField(key = "price", required = true)
    private final BigDecimal price;

    @BodyField(key = "size", required = true)
    private final BigDecimal orderSize;

    @BodyField(key = "type", required = true)
    private final OrderType orderType = OrderType.LIMIT;

    @BodyField(key = "client_oid")
    private final UUID orderId;

    @BodyField(key = "stop")
    private final OrderStop orderStop;

    @BodyField(key = "stop_price")
    private final BigDecimal stopPrice;

    @BodyField(key = "stp")
    private final SelfTradePrevention selfTradePrevention;

    @BodyField(key = "time_in_force")
    private final TimeInForce timeInForce;

    @BodyField(key = "cancel_after")
    private final DateTime cancelAfter;

    @BodyField(key = "post_only")
    private final Boolean postOnly;

    /**
     * Validates that when these fields are present:
     * <p> 1. <code>cancelAfter</code>: <code>timeInForce</code> is GTT
     * <p> 2. <code>postOnly</code>: <code>timeInForce</code> is not IOC or FOK
     *
     * @return True if limit order constraints are met, otherwise false.
     */
    @Override
    public boolean isValidRequest() {
        return allOrNothing(stopPrice, orderStop)
                && (ifPresent(timeInForce, TimeInForce.GTT::equals)
                    || ifPresent(postOnly, p -> !(TimeInForce.IOC == timeInForce || TimeInForce.FOK == timeInForce)));
    }
}

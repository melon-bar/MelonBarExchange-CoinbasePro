package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.BodyField;
import com.coinbase.exchange.model.Product;
import com.coinbase.exchange.model.order.flag.OrderSide;
import com.coinbase.exchange.model.order.flag.OrderStop;
import com.coinbase.exchange.model.order.flag.OrderType;
import com.coinbase.exchange.model.order.flag.SelfTradePrevention;
import com.coinbase.exchange.model.request.BaseRequest;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public class MarketOrderRequest extends BaseRequest {

    @BodyField(key = "side", required = true)
    private final OrderSide orderSide;

    @BodyField(key = "product_id", required = true)
    private final Product product;

    @BodyField(key = "type", required = true)
    private final OrderType orderType = OrderType.MARKET;

    @BodyField(key = "size")
    private final BigDecimal size;

    @BodyField(key = "client_oid")
    private final UUID orderId;

    @BodyField(key = "stop")
    private final OrderStop orderStop;

    @BodyField(key = "stop_price")
    private final BigDecimal stopPrice;

    @BodyField(key = "stp")
    private final SelfTradePrevention selfTradePrevention;

    @BodyField(key = "funds")
    private final BigDecimal funds;

    /**
     * Is a valid request when:
     * <p> 1. Either <code>stopPrice</code> or <code>orderPrice</code> is defined, but never both
     * <p> 2. One of <code>size</code> or <code>funds</code> is defined
     *
     * @return True if market order constraints are met, otherwise false.
     */
    @Override
    public boolean isValidRequest() {
        return allOrNothing(stopPrice, orderStop) && !(size != null && funds != null);
    }
}

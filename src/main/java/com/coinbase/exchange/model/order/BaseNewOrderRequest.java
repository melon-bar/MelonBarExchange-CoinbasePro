package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.BodyField;
import com.coinbase.exchange.model.request.BaseRequest;
import com.coinbase.exchange.model.Product;
import com.coinbase.exchange.model.order.flag.OrderSide;
import com.coinbase.exchange.model.order.flag.OrderStop;
import com.coinbase.exchange.model.order.flag.OrderType;
import com.coinbase.exchange.model.order.flag.SelfTradePrevention;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public abstract class BaseNewOrderRequest extends BaseRequest {

    @BodyField(key = "side", required = true)
    private final OrderSide orderSide;

    @BodyField(key = "product_id", required = true)
    private final Product product;

    @BodyField(key = "client_oid")
    private final UUID clientOrderId;

    @BodyField(key = "type")
    private final OrderType orderType;

    @BodyField(key = "stop")
    private final OrderStop orderStop;

    @BodyField(key = "stop_price")
    private final BigDecimal stopPrice;

    @BodyField(key = "stp")
    private final SelfTradePrevention selfTradePrevention;

    protected boolean validateBaseRequest() {
        return allOrNothing(stopPrice, orderStop);
    }
}

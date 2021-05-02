package com.coinbase.exchange.model.order;

import com.coinbase.exchange.annotation.BodyField;
import com.coinbase.exchange.model.Product;
import com.coinbase.exchange.model.order.flag.OrderSide;
import com.coinbase.exchange.model.order.flag.OrderStop;
import com.coinbase.exchange.model.order.flag.OrderType;
import com.coinbase.exchange.model.order.flag.SelfTradePrevention;
import com.coinbase.exchange.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseNewOrderRequest extends BaseRequest {

    @BodyField(key = "side", required = true)
    private OrderSide orderSide;

    @BodyField(key = "product_id", required = true)
    private Product product;

    @BodyField(key = "client_oid")
    private UUID orderId;

    @BodyField(key = "type")
    private OrderType orderType;

    @BodyField(key = "stop")
    private OrderStop orderStop;

    @BodyField(key = "stop_price")
    private BigDecimal stopPrice;

    @BodyField(key = "stp")
    private SelfTradePrevention selfTradePrevention;

    protected boolean validateBaseRequest() {
        return allOrNothing(stopPrice, orderStop);
    }
}

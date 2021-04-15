package com.coinbase.model.order.request;

import com.coinbase.model.Product;
import com.coinbase.model.request.BaseRequest;
import com.coinbase.model.annotation.Required;
import com.coinbase.model.order.flag.OrderSide;
import com.coinbase.model.order.flag.OrderStop;
import com.coinbase.model.order.flag.OrderType;
import com.coinbase.model.order.flag.SelfTradePrevention;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public abstract class BaseNewOrderRequest extends BaseRequest {

    @Required
    private final OrderSide orderSide;

    @Required
    private final Product product;

    private UUID clientOrderId;
    private OrderType orderType;
    private OrderStop orderStop;
    private BigDecimal stopPrice;
    private SelfTradePrevention selfTradePrevention;

    protected boolean validateBaseRequest() {
        return ifPresent(orderStop, (__) -> stopPrice != null) // TODO: there is a better way to do this...
                && ifPresent(stopPrice, (__) -> orderStop != null);
    }
}

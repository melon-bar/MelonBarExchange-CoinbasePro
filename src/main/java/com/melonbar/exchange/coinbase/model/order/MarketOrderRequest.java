package com.melonbar.exchange.coinbase.model.order;

import com.melonbar.core.model.ProductId;
import com.melonbar.exchange.coinbase.annotation.BodyField;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import com.melonbar.exchange.coinbase.model.order.flag.OrderStop;
import com.melonbar.exchange.coinbase.model.order.flag.OrderType;
import com.melonbar.exchange.coinbase.model.order.flag.SelfTradePrevention;
import com.melonbar.core.http.request.BaseRequest;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public class MarketOrderRequest extends BaseRequest {

    @BodyField(key = "side", required = true)
    private final OrderSide side;

    @BodyField(key = "product_id", required = true)
    private final ProductId productId;

    @BodyField(key = "type", required = true)
    private final OrderType type = OrderType.MARKET;

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

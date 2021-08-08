package com.melonbar.exchange.coinbase.websocket.message.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.core.model.ProductId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Channel {

    public static final Channel HEARTBEAT = new Channel(ChannelType.HEARTBEAT, null);
    public static final Channel STATUS = new Channel(ChannelType.STATUS, null);
    public static final Channel TICKER = new Channel(ChannelType.TICKER, null);
    public static final Channel LEVEL2 = new Channel(ChannelType.LEVEL2, null);
    public static final Channel USER = new Channel(ChannelType.USER, null);
    public static final Channel MATCHES = new Channel(ChannelType.MATCHES, null);
    public static final Channel FULL = new Channel(ChannelType.FULL, null);


    @JsonProperty("name") private ChannelType name;
    @JsonProperty("product_ids") private ProductId[] productIds;

    public Channel withProductIds(final ProductId ... productIds) {
        if (productIds != null && productIds.length > 0) {
            this.productIds = new ProductId[productIds.length];
            ArrayUtils.addAll(this.productIds, productIds);
        }
        return this;
    }
}

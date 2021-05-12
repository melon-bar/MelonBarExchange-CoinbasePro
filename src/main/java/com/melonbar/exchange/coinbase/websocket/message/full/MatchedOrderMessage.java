package com.melonbar.exchange.coinbase.websocket.message.full;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public class MatchedOrderMessage extends FullFeedMessage {

    @JsonProperty("trade_id") private Long tradeId;
    @JsonProperty("maker_order_id") private String makerOrderId;
    @JsonProperty("taker_order_id") private String takerOrderId;

    // authenticated fields

    @JsonProperty("taker_user_id") private String takerUserId;
    @JsonProperty("taker_profile_id") private String takerProfileId;
    @JsonProperty("taker_fee_rate") private BigDecimal takerFeeRate;

    @JsonProperty("maker_user_id") private String makerUserId;
    @JsonProperty("maker_profile_id") private String makerProfileId;
    @JsonProperty("maker_fee_rate") private BigDecimal makerFeeRate;

    @JsonProperty("user_id") private String userId;
    @JsonProperty("profile_id") private String profileId;
}

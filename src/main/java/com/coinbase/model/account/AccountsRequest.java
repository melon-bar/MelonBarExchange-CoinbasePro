package com.coinbase.model.account;

import com.coinbase.model.annotation.Field;
import com.coinbase.model.request.BaseRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountsRequest extends BaseRequest {

    @Field(name = "id", type = String.class)
    private final String accountId;
}

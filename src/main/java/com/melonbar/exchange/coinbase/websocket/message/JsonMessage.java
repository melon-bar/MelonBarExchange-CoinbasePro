package com.melonbar.exchange.coinbase.websocket.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.melonbar.exchange.coinbase.websocket.message.deserializer.JsonMessageMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TickerMessage.class, name = "ticker")
})
public abstract class JsonMessage implements Message {

    @JsonIgnore
    @Override
    public String getText() {
        return JsonMessageMapper.objectToJson(this);
    }
}

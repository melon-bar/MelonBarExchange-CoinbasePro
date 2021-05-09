package com.melonbar.exchange.coinbase.websocket.message.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.melonbar.exchange.coinbase.model.core.Product;
import com.melonbar.exchange.coinbase.websocket.message.JsonMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JsonMessageMapper {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        final SimpleModule productModule = new SimpleModule();
        productModule.addDeserializer(Product.class, new ProductDeserializer());

        OBJECT_MAPPER.registerModule(productModule);
        OBJECT_MAPPER.registerModule(new JodaModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static <T extends JsonMessage> String objectToJson(final T jsonMessage) {
        try {
            return OBJECT_MAPPER.writer().writeValueAsString(jsonMessage);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Something went wrong while processing [{}] as json", jsonMessage.getClass().getName(),
                    jsonProcessingException);
        }
        return "{}";
    }

    public static <T extends JsonMessage> Optional<T> jsonToObject(final String jsonString, final Class<T> clazz) {
        try {
            return Optional.ofNullable(OBJECT_MAPPER.reader().readValue(jsonString, clazz));
        } catch (IOException ioException) {
            log.error("Something went wrong while converting json [{}] to type [{}]", jsonString,
                    clazz.getName(), ioException);
        }
        return Optional.empty();
    }
}

package com.melonbar.exchange.coinbase.websocket.message.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.websocket.MessageTypes;
import com.melonbar.exchange.coinbase.websocket.message.FeedMessage;
import com.melonbar.exchange.coinbase.websocket.message.model.L2OrderTuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JsonMessageMapper {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final static String TYPE_FIELD = "\"type\":";

    static {
        final SimpleModule websocketFeedMessageModule = new SimpleModule();
        websocketFeedMessageModule.addDeserializer(ProductId.class, new ProductIdDeserializer());
        websocketFeedMessageModule.addDeserializer(L2OrderTuple.class, new OrderDeserializer());

        OBJECT_MAPPER.registerModule(websocketFeedMessageModule);
        OBJECT_MAPPER.registerModule(new JodaModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static <T extends FeedMessage> String objectToJson(final T jsonMessage) {
        try {
            return OBJECT_MAPPER.writer().writeValueAsString(jsonMessage);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Something went wrong while processing [{}] as json", jsonMessage.getClass().getName(),
                    jsonProcessingException);
        }
        return "{}";
    }

    public static <T extends FeedMessage> Optional<T> jsonToObject(final String jsonString, final Class<T> clazz) {
        try {
            return Optional.ofNullable(OBJECT_MAPPER.reader().readValue(jsonString, clazz));
        } catch (IOException ioException) {
            log.error("Something went wrong while converting json [{}] to type [{}]", jsonString,
                    clazz.getName(), ioException);
        }
        return Optional.empty();
    }

    public static Optional<? extends FeedMessage> jsonToObject(final String jsonString) {
        final String type = extractType(jsonString);
        return StringUtils.isEmpty(type)
                ? Optional.empty()
                : jsonToObject(jsonString, MessageTypes.evaluateMessageType(type));
    }

    private static String extractType(final String jsonString) {
        final String stripped = jsonString.replaceAll("\\s", "");
        final int typeFieldIndex = stripped.indexOf(TYPE_FIELD);
        if (typeFieldIndex < 0) {
            return null;
        }
        // if there exists only 1 json field, suffix will be close bracket instead of comma
        final int endIndex = stripped.indexOf(stripped.indexOf(',') > 0 ? "," : "}", typeFieldIndex)-1;
        return stripped
                .substring(typeFieldIndex+TYPE_FIELD.length()+1, endIndex)
                .trim();
    }
}

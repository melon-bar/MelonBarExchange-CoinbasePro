package com.melonbar.exchange.coinbase.websocket.message.deserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.util.JsonUtils;
import com.melonbar.exchange.coinbase.websocket.MessageTypes;
import com.melonbar.exchange.coinbase.websocket.message.FeedMessage;
import com.melonbar.exchange.coinbase.websocket.message.model.L2OrderTuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * Utility class intended to statically marshal and unmarshal json messages based on {@link FeedMessage} definitions.
 * Internally uses {@link ObjectMapper} in a way that is thread-safe. This thread safety is guaranteed by provided
 * delegators for serialize/deserialize via {@link ObjectMapper#writer} and {@link ObjectMapper#reader}, which are
 * designed to be thread-safe. Because of this, we only need to define one instance of {@link ObjectMapper} and apply
 * the necessary modules once.
 *
 * <p> All necessary modules, instances of {@link com.fasterxml.jackson.databind.Module}, are registered to the
 * {@link ObjectMapper} statically. Currently, custom modules are needed to handle types:
 * <ul>
 *     <li>{@link ProductId}</li>
 *     <li>{@link L2OrderTuple}</li>
 *     <li>{@link org.joda.time.DateTime Joda DateTime}</li>
 * </ul>
 */
@Slf4j
public class JsonMessageMapper {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        final SimpleModule websocketFeedMessageModule = new SimpleModule();
        websocketFeedMessageModule.addDeserializer(ProductId.class, new ProductIdDeserializer());
        websocketFeedMessageModule.addDeserializer(L2OrderTuple.class, new OrderDeserializer());

        OBJECT_MAPPER.registerModule(websocketFeedMessageModule);
        OBJECT_MAPPER.registerModule(new JodaModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Marshals the input {@link FeedMessage} into a json string.
     *
     * @param jsonMessage {@link FeedMessage} instance to marshal
     * @param <T> Extension of {@link FeedMessage}
     * @return {@link FeedMessage} as json string
     * @see JsonProperty Annotation for marking member fields for handling
     */
    public static <T extends FeedMessage> String objectToJson(final T jsonMessage) {
        try {
            return OBJECT_MAPPER.writer().writeValueAsString(jsonMessage);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Something went wrong while processing [{}] as json", jsonMessage.getClass().getName(),
                    jsonProcessingException);
        }
        return "{}";
    }

    /**
     * Unmarshals the input json string into a {@link FeedMessage} instance. Uses the input class to determine
     * which extension of {@link FeedMessage} to unmarshal the json string into.
     *
     * @param jsonString Json string to unmarshal
     * @param clazz Class to unmarshal into
     * @param <T> Extension of {@link FeedMessage}
     * @return Instance of <code>clazz</code>, an extension of {@link FeedMessage}
     * @see JsonProperty Annotation for marking member fields for handling
     */
    public static <T extends FeedMessage> Optional<T> jsonToObject(final String jsonString, final Class<T> clazz) {
        try {
            return Optional.ofNullable(OBJECT_MAPPER.reader().readValue(jsonString, clazz));
        } catch (IOException ioException) {
            log.error("Something went wrong while converting json [{}] to type [{}]", jsonString,
                    clazz.getName(), ioException);
        }
        return Optional.empty();
    }

    /**
     * Unmarshals the input json string into a {@link FeedMessage} instance, similar to
     * {@link #jsonToObject(String, Class)}. However, this implementation differs in that it dynamically detects
     * the desired class to unmarshal based on the json string's <code>type</code> field.
     *
     * @param jsonString Json string
     * @return {@link Optional} of {@link FeedMessage}, {@link Optional#empty} if json string is empty
     * @see MessageTypes
     */
    public static Optional<? extends FeedMessage> jsonToObject(final String jsonString) {
        final String type = JsonUtils.extractField(FeedMessage.TYPE_FIELD, jsonString);
        return StringUtils.isEmpty(type)
                ? Optional.empty()
                : jsonToObject(jsonString, MessageTypes.evaluateMessageType(type));
    }
}

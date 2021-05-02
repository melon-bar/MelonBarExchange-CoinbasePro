package com.coinbase.exchange.util.response;

import com.coinbase.exchange.model.response.PostProcessor;
import com.coinbase.exchange.util.Format;
import com.coinbase.exchange.util.Guard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

/**
 * Static library of common implementations of {@link PostProcessor} and other utility methods.
 */
@Slf4j
public final class PostProcessing {

    /**
     * Convert response body content to an {@link Optional} containing {@link JsonNode}, where the value is
     * present if the content can be successfully parsed as a json object.
     *
     * @return {@link PostProcessor} that provides <code>Optional{@literal <}JsonNode{@literal >}</code>
     */
    public static PostProcessor<Optional<JsonNode>> asJson() {
        return response -> Optional.ofNullable(marshalJson(response.content()));
    }

    /**
     * Converts the response content into a {@link JsonNode} using <code>asJson()</code> first, then attempts
     * to extract the value as a string using parameter <code>key</code> as a key.
     *
     * @param key
     * @return
     */
    public static PostProcessor<String> getJsonValueAsString(final String key) {
        return response -> asJson()
                .andThen(jsonNode -> jsonNode
                        .map(_jsonNode -> _jsonNode.get(key))
                        .map(JsonNode::toString)
                        // key does not exist in json node, or failed to extract due to other issues (failed parse)
                        .orElseThrow(() -> new RuntimeException(Format.format(
                                "Could not extract key [{}] from response content [{}]", key, response.content()))))
                .apply(response);
    }

    private static JsonNode marshalJson(final String jsonString) {
        Guard.nonNull(jsonString);
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(
                    objectMapper.getFactory().createParser(jsonString));
        } catch (IOException ioException) {
            log.error("Exception [{}] thrown while attempting to parse content as json: [{}]",
                    ioException.getClass().getName(), jsonString, ioException);
        }
        return null;
    }
}

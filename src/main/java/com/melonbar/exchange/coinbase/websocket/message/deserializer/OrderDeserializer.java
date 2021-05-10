package com.melonbar.exchange.coinbase.websocket.message.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.melonbar.exchange.coinbase.util.Format;
import com.melonbar.exchange.coinbase.websocket.message.model.L2OrderTuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@Slf4j
public class OrderDeserializer extends JsonDeserializer<L2OrderTuple> {

    @Override
    public L2OrderTuple deserialize(final JsonParser jsonParser, final DeserializationContext __)
            throws IOException {
        final String text = jsonParser.readValueAsTree()
                .toString()
                .replaceAll("\"", "");
        if (StringUtils.isEmpty(text) || (text.charAt(0) != '[' && text.charAt(text.length()-1) != ']')) {
            throw new IllegalArgumentException(
                    Format.format("Unexpected json string provided for [{}] deserialization: [{}]",
                            this.getClass().getName(), text));
        }
        return L2OrderTuple.valueOf(
                text.substring(1, text.length()-1).split(L2OrderTuple.DELIMITER));
    }
}
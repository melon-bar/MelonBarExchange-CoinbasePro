package com.melonbar.exchange.coinbase.websocket.message.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.util.Format;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ProductIdDeserializer extends JsonDeserializer<ProductId> {

    @Override
    public ProductId deserialize(final JsonParser jsonParser, final DeserializationContext __)
            throws IOException {
        if (StringUtils.isEmpty(jsonParser.getText()) || !jsonParser.getText().contains(ProductId.DELIMITER)) {
            throw new IllegalArgumentException(Format.format("Invalid text [{}] for Product deserialization",
                    jsonParser.getText()));
        }
        final String[] split = jsonParser.getText().split(ProductId.DELIMITER);
        return ProductId.of(split[0], split[1]);
    }
}

package com.melonbar.exchange.coinbase.websocket.message.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.melonbar.exchange.coinbase.model.core.Product;
import com.melonbar.exchange.coinbase.util.Format;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ProductDeserializer extends JsonDeserializer<Product> {

    @Override
    public Product deserialize(final JsonParser jsonParser, final DeserializationContext __)
            throws IOException {
        if (StringUtils.isEmpty(jsonParser.getText()) || !jsonParser.getText().contains(Product.DELIMITER)) {
            throw new IllegalStateException(Format.format("Invalid text [{}] for Product deserialization",
                    jsonParser.getText()));
        }
        final String[] split = jsonParser.getText().split(Product.DELIMITER);
        return Product.of(split[0], split[1]);
    }
}

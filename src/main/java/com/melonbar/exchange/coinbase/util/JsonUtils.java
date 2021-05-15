package com.melonbar.exchange.coinbase.util;

/**
 * Simple static utilities for json string manipulation. All methods should be stateless, and thus thread-safe.
 */
public final class JsonUtils {

    /**
     * Extracts the string value from the input <code>jsonString</code> using input <code>key</code> as
     * the json key. The input key should be the raw string value, and not contain any extraneous quotations or
     * semicolons since this method handles adding it in before parsing. Furthermore, it is up to the caller to
     * ensure the input json string is in a valid json format, otherwise the resultant behavior is undefined.
     *
     * <p> Basic usage example:
     * <pre>
     *     String jsonString = "{\"key0\":\"value0\", \"key1\":1001}"
     *     extractField("key0", jsonString); // outputs "value0", quotes stripped
     *     extractField("key1", jsonString); // outputs "1001"
     * </pre>
     *
     * @param key Key for value to be extracted
     * @param jsonString Json string
     * @return Json value
     */
    public static String extractField(final String key, final String jsonString) {
        Guard.nonNull(key, jsonString);
        final String stripped = jsonString.replaceAll("\\s", "");
        final String quotedKey = '\"' + key + '\"' + ':';
        final int typeFieldIndex = stripped.indexOf(quotedKey);
        if (typeFieldIndex < 0) {
            return null;
        }
        // determine beginning and end indices for value extraction
        int startIndex = typeFieldIndex + quotedKey.length();
        int endIndex = stripped.indexOf(
                // if no comma present after start index, then assume key is last element
                stripped.indexOf(',', startIndex) > 0 ? "," : "}", typeFieldIndex);
        // determine if quotes must be stripped
        if (stripped.charAt(startIndex) == '\"') {
            startIndex += 1;
            endIndex -= 1;
        }
        return stripped
                .substring(startIndex, endIndex)
                .trim();
    }
}

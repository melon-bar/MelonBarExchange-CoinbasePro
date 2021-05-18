package com.melonbar.exchange.coinbase.util;

/**
 * Simple static utilities for json string manipulation. All methods should be stateless, and thus thread-safe.
 */
public final class JsonUtils {

    private static final char QUOTE = '\"';
    private static final char SEMICOLON = ':';
    private static final char COMMA = ',';
    private static final char CLOSE_BRACKET = '}';

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
        final String quotedKey = QUOTE + key + QUOTE + SEMICOLON;
        final int keyIndex = stripped.indexOf(quotedKey);
        if (keyIndex < 0) {
            return null;
        }
        // determine beginning and end indices for value extraction
        int startIndex = keyIndex + quotedKey.length();
        int endIndex = stripped.indexOf(
                // if no comma present after start index, then assume key is last element
                stripped.indexOf(COMMA, startIndex) > 0 ? COMMA : CLOSE_BRACKET, keyIndex);
        // determine if quotes must be stripped
        if (stripped.charAt(startIndex) == QUOTE) {
            startIndex += 1;
            endIndex -= 1;
        }
        return stripped
                .substring(startIndex, endIndex)
                .trim();
    }

    /**
     * Wrapped call to {@link #extractField(String key, String jsonString)} but provides a fallback value when the
     * result is null.
     *
     * @param key Key for value to be extracted
     * @param jsonString Json string
     * @param defaultValue Fallback value
     * @return Json value if present, otherwise <code>defaultValue</code>
     */
    public static String extractField(final String key, final String jsonString,
                                      final String defaultValue) {
        final String value = extractField(key, jsonString);
        return value == null ? defaultValue : value;
    }

    /**
     * Returns array of strings containing all extracted fields. The resultant array is the same size
     * as the number of args for <code>fields</code>. If not all values are present, an exception will
     * be thrown.
     *
     * @param jsonString Json string
     * @param fields Fields to search for
     * @return Array of values
     * @throws IllegalArgumentException When any of the input fields cannot be found
     */
    public static String[] extractFields(final String jsonString, final String ... fields) {
        return extractFields(true, jsonString, fields);
    }

    /**
     * Returns array of strings containing extracted fields. If a field could not be found, the corresponding
     * value in the resultant array will be <code>null</code>.
     *
     * @param jsonString Json string
     * @param fields Fields to search for
     * @return Array of values, <code>null</code> element for each non-present field
     */
    public static String[] extractFieldsUnsafe(final String jsonString, final String ... fields) {
        return extractFields(false, jsonString, fields);
    }

    /**
     * Searches for each field in the json string, and conditionally throws {@link IllegalArgumentException} for
     * non-present fields.
     *
     * @param allOrNothing When true, throws {@link IllegalArgumentException} for non-present fields
     * @param jsonString Json string
     * @param fields Fields to search for
     * @return Array of values
     * @throws IllegalArgumentException When any of the input fields cannot be found and
     *  <code>allOrNothing</code> is true
     */
    private static String[] extractFields(final boolean allOrNothing, final String jsonString,
                                          final String ... fields) {
        final String[] values = new String[fields.length];
        for (int i = 0; i < values.length; i++) {
            values[i++] = extractField(fields[i], jsonString);
            if (allOrNothing && values[i] == null) {
                throw new IllegalArgumentException(
                        Format.format("Could not find field [{}] in json [{}]", fields[i], jsonString));
            }
        }
        return values;
    }
}

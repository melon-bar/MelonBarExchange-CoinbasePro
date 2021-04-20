package com.coinbase.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * Helper class intended for static use to perform basic validation checks for core logic.
 */
public final class Guard {

    /**
     * Ensures all input parameters are non-null. Loops through each param and invokes {@link Objects#requireNonNull}.
     * Short circuits on the first null param, so if there were more than 1 null input, only the first null occurrence
     * would be logged.
     *
     * @param objects List of parameters to verify as non-null
     * @throws NullPointerException On the first null input
     */
    public static void nonNull(final Object ... objects) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                Objects.requireNonNull(objects[i], Format.format("Null object found in {} at i={}",
                        Arrays.toString(objects), i));
            }
        }
    }
}

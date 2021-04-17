package com.coinbase.util;

import java.util.Arrays;
import java.util.Objects;

public final class Guard {

    public static void nonNull(final Object ... objects) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                Objects.requireNonNull(objects[i], Format.format("Null object found in {} at i={}",
                        Arrays.toString(objects), i));
            }
        }
    }
}

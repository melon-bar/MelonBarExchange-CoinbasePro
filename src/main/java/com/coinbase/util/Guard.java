package com.coinbase.util;

import java.util.Arrays;

public final class Guard {

    public static void notNull(final Object ... objects) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                throw new NullPointerException(Format.format("Null object found in {} at i={}",
                        Arrays.toString(objects), i));
            }
        }
    }
}

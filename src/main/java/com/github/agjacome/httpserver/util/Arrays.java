package com.github.agjacome.httpserver.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.agjacome.httpserver.util.annotation.DisallowConstruction;

/**
 * {@linkplain Arrays} holds static utility methods for manipulating and
 * transforming Arrays, that are not covered by Java's default
 * {@link java.util.Arrays} class.
 */
public final class Arrays {

    @DisallowConstruction
    private Arrays() { }

    @SafeVarargs
    public static <A> Set<A> asSet(final A ... as) {
        final Set<A> result = new HashSet<>(as.length);
        Collections.addAll(result, as);
        return result;
    }

}

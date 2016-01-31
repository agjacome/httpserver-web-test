package com.github.agjacome.httpserver.util.contract;

import java.util.Objects;

import com.github.agjacome.httpserver.util.annotation.DisallowConstruction;

import static java.util.Objects.requireNonNull;

/**
 * {@linkplain NullContracts} holds static utility methods for contract
 * verification of null arguments. It can be thought as an extensions to Java's
 * standard {@link Objects#requireNonNull(Object)}.
 */
public final class NullContracts {

    @DisallowConstruction
    private NullContracts() { }

    public static <A> A[] requireAllNonNull(
        final A[ ] all, final String format, final Object ... args
    ) throws NullPointerException {
        final String message = String.format(format, args);

        for (final A one : all)
            requireNonNull(one, message);

        return all;
    }

    @SafeVarargs
    public static <A> A[] requireAllNonNull(
        final A ... all
    ) throws NullPointerException {
        return requireAllNonNull(
            all, "Expected all values to be non-null, but null value found"
        );
    }

}

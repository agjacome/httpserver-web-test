package com.github.agjacome.httpserver.util.contract;

import com.github.agjacome.httpserver.util.annotation.DisallowConstruction;

/**
 * {@linkplain StringContracts} holds static utility methods for contract
 * verification of String arguments.
 */
public final class StringContracts {

    @DisallowConstruction
    private StringContracts() { }

    public static String requireNonEmpty(
        final String string, final String format, final Object ... args
    ) throws IllegalArgumentException {
        if (string.isEmpty())
            throw new IllegalArgumentException(String.format(format, args));

        return string;
    }

    public static String requireNonEmpty(
        final String string
    ) throws IllegalArgumentException {
        return requireNonEmpty(string, "Expected non-empty string");
    }

    public static String requireExactLength(
        final String     string,
        final int        length,
        final String     format,
        final Object ... args
    ) throws IllegalArgumentException {
        if (string.length() != length)
            throw new IllegalArgumentException(String.format(format, args));

        return string;
    }

    public static String requireExactLength(
        final String string, final int length
    ) throws IllegalArgumentException {
        return requireExactLength(
            string, length,
            "Expected string of length %d, but '%s' (%d characters) given",
            length, string, string.length()
        );
    }

}

package com.github.agjacome.httpserver.util.contract;

import com.github.agjacome.httpserver.util.annotation.DisallowConstruction;

/**
 * {@linkplain NumericContracts} holds static utility methods for contract
 * verification of numeric arguments.
 */
public final class NumericContracts {

    @DisallowConstruction
    private NumericContracts() { }

    public static int requireNonNegative(
        final int number, final String format, final Object ... args
    ) throws IllegalArgumentException {
        if (number < 0)
            throw new IllegalArgumentException(String.format(format, args));
        else
            return number;
    }

    public static int requireNonNegative(
        final int number
    ) throws IllegalArgumentException {
        return requireNonNegative(
            number, "Expected non-negative value, %d given", number
        );
    }

    public static int requirePositive(
        final int number, final String format, final Object ... args
    ) throws IllegalArgumentException {
        if (number <= 0)
            throw new IllegalArgumentException(String.format(format, args));
        else
            return number;
    }

    public static int requirePositive(
        final int number
    ) throws IllegalArgumentException {
        return requirePositive(
            number, "Expected positive value, %d given", number
        );
    }

    public static void requireEqual(
        final int        number1,
        final int        number2,
        final String     format,
        final Object ... args
    ) throws IllegalArgumentException {
        if (number1 != number2)
            throw new IllegalArgumentException(String.format(format, args));
    }

    public static void requireEqual(
        final int number1, final int number2
    ) throws IllegalArgumentException {
        requireEqual(
            number1, number2, "Expected %d, but %d given", number1, number2
        );
    }

}

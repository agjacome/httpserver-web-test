package com.github.agjacome.httpserver.util.contract;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.java.lang.Encoded;
import com.pholser.junit.quickcheck.generator.java.lang.Encoded.InCharset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;

import static com.github.agjacome.httpserver.util.contract.NumericContracts.requireEqual;
import static com.github.agjacome.httpserver.util.contract.NumericContracts.requireNonNegative;
import static com.github.agjacome.httpserver.util.contract.NumericContracts.requirePositive;

@RunWith(Theories.class)
public class NumericContractsTest {

    @Theory
    public void require_non_negative_must_throw_exception_on_negative_integer(
        @ForAll @InRange(max = "-1") final int number
    ) {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonNegative(number));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonNegative(number, "test"));
    }

    @Theory
    public void require_non_negative_must_return_given_integer_if_non_negative(
        @ForAll @InRange(min = "0") final int number
    ) {
        assertThat(requireNonNegative(number        )).isEqualTo(number);
        assertThat(requireNonNegative(number, "test")).isEqualTo(number);
    }

    @Theory
    public void require_non_negative_thrown_exception_must_have_given_formatted_message(
        @ForAll(sampleSize = 10) @InRange(max = "-1") final int number,
        @ForAll(sampleSize =  2) final String message
    ) {
        final String expectedMessage = String.format("MSG: %s", message);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonNegative(number, "MSG: %s", message))
            .withMessage(expectedMessage);
    }

    @Theory
    public void require_non_negative_thrown_exception_must_have_default_message_if_none_given(
        @ForAll @InRange(max = "-1") final int number
    ) {
        final String expectedMessage = String.format(
            "Expected non-negative value, %d given", number
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonNegative(number))
            .withMessage(expectedMessage);
    }

    @Theory
    public void require_positive_must_throw_exception_on_positive_integer(
        @ForAll @InRange(max = "0") final int number
    ) {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requirePositive(number));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requirePositive(number, "test"));
    }

    @Theory
    public void require_positive_must_return_given_integer_if_positive(
        @ForAll @InRange(min = "1") final int number
    ) {
        assertThat(requirePositive(number        )).isEqualTo(number);
        assertThat(requirePositive(number, "test")).isEqualTo(number);
    }

    @Theory
    public void require_positive_thrown_exception_must_have_given_formatted_message(
        @ForAll(sampleSize = 10) @InRange(max = "0") final int number,
        @ForAll(sampleSize =  2) final String message
    ) {
        final String expectedMessage = String.format("MSG: %s", message);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requirePositive(number, "MSG: %s", message))
            .withMessage(expectedMessage);
    }

    @Theory
    public void require_positive_thrown_exception_must_have_default_message_if_none_given(
        @ForAll @InRange(max = "0") final int number
    ) {
        final String expectedMessage = String.format(
            "Expected positive value, %d given", number
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requirePositive(number))
            .withMessage(expectedMessage);
    }

    @Theory
    public void require_equal_must_throw_exception_on_non_equal_integers(
        @ForAll final int number1, @ForAll final int number2
    ) {
        assumeThat(number1, is(not(number2)));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireEqual(number1, number2));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireEqual(number1, number2, "test"));
    }

    @Theory
    public void require_equal_must_not_throw_on_equal_integers(
        @ForAll final int number
    ) {
        requireEqual(number, number);
        requireEqual(number, number, "test");
    }

    @Theory
    public void require_equal_thrown_exception_must_have_given_formatted_message(
        @ForAll(sampleSize = 5) final int number1,
        @ForAll(sampleSize = 5) final int number2,
        @ForAll(sampleSize = 2) @From(Encoded.class) @InCharset("US-ASCII") final String message
    ) {
        assumeThat(number1, is(not(number2)));

        final String expectedMessage = String.format("MSG: %s", message);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireEqual(number1, number2, "MSG: %s", message))
            .withMessage(expectedMessage);
    }

    @Theory
    public void require_equal_thrown_exception_must_have_default_message_if_none_given(
        @ForAll final int number1, @ForAll final int number2
    ) {
        assumeThat(number1, is(not(number2)));

        final String expectedMessage = String.format(
            "Expected %d, but %d given", number1, number2
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireEqual(number1, number2))
            .withMessage(expectedMessage);
    }

}

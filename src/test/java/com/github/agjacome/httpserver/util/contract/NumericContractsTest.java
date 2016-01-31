package com.github.agjacome.httpserver.util.contract;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;

import static com.github.agjacome.httpserver.util.contract.NumericContracts.requireEqual;
import static com.github.agjacome.httpserver.util.contract.NumericContracts.requireNonNegative;
import static com.github.agjacome.httpserver.util.contract.NumericContracts.requirePositive;

@RunWith(JUnitQuickcheck.class)
public class NumericContractsTest {

    @Property
    public void require_non_negative_must_throw_exception_on_negative_integer(
        @InRange(max = "-1") final int number
    ) {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonNegative(number));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonNegative(number, "test"));
    }

    @Property
    public void require_non_negative_must_return_given_integer_if_non_negative(
        @InRange(min = "0") final int number
    ) {
        assertThat(requireNonNegative(number        )).isEqualTo(number);
        assertThat(requireNonNegative(number, "test")).isEqualTo(number);
    }

    @Property
    public void require_non_negative_thrown_exception_must_have_given_formatted_message(
        @InRange(max = "-1") final int number, final String message
    ) {
        final String expectedMessage = String.format("MSG: %s", message);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonNegative(number, "MSG: %s", message))
            .withMessage(expectedMessage);
    }

    @Property
    public void require_non_negative_thrown_exception_must_have_default_message_if_none_given(
        @InRange(max = "-1") final int number
    ) {
        final String expectedMessage = String.format(
            "Expected non-negative value, %d given", number
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonNegative(number))
            .withMessage(expectedMessage);
    }

    @Property
    public void require_positive_must_throw_exception_on_positive_integer(
        @InRange(max = "0") final int number
    ) {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requirePositive(number));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requirePositive(number, "test"));
    }

    @Property
    public void require_positive_must_return_given_integer_if_positive(
        @InRange(min = "1") final int number
    ) {
        assertThat(requirePositive(number        )).isEqualTo(number);
        assertThat(requirePositive(number, "test")).isEqualTo(number);
    }

    @Property
    public void require_positive_thrown_exception_must_have_given_formatted_message(
        @InRange(max = "0") final int number, final String message
    ) {
        final String expectedMessage = String.format("MSG: %s", message);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requirePositive(number, "MSG: %s", message))
            .withMessage(expectedMessage);
    }

    @Property
    public void require_positive_thrown_exception_must_have_default_message_if_none_given(
        @InRange(max = "0") final int number
    ) {
        final String expectedMessage = String.format(
            "Expected positive value, %d given", number
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requirePositive(number))
            .withMessage(expectedMessage);
    }

    @Property
    public void require_equal_must_throw_exception_on_non_equal_integers(
        final int number1, final int number2
    ) {
        assumeThat(number1, is(not(number2)));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireEqual(number1, number2));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireEqual(number1, number2, "test"));
    }

    @Property
    public void require_equal_must_not_throw_on_equal_integers(
        final int number
    ) {
        requireEqual(number, number);
        requireEqual(number, number, "test");
    }

    @Property
    public void require_equal_thrown_exception_must_have_given_formatted_message(
        final int number1, final int number2, final String message
    ) {
        assumeThat(number1, is(not(number2)));

        final String expectedMessage = String.format("MSG: %s", message);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireEqual(number1, number2, "MSG: %s", message))
            .withMessage(expectedMessage);
    }

    @Property
    public void require_equal_thrown_exception_must_have_default_message_if_none_given(
        final int number1, final int number2
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

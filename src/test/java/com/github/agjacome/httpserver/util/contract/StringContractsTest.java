package com.github.agjacome.httpserver.util.contract;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;

import static com.github.agjacome.httpserver.util.contract.StringContracts.requireExactLength;
import static com.github.agjacome.httpserver.util.contract.StringContracts.requireNonEmpty;

@RunWith(JUnitQuickcheck.class)
public class StringContractsTest {

    @Test
    public void require_non_empty_must_throw_exception_on_empty_string() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonEmpty(""));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonEmpty("", "test"));
    }

    @Property
    public void require_non_empty_must_return_given_string_if_not_empty(
        final String string
    ) {
        assumeThat(string.isEmpty(), is(false));

        assertThat(requireNonEmpty(string        )).isEqualTo(string);
        assertThat(requireNonEmpty(string, "test")).isEqualTo(string);
    }

    @Property
    public void require_non_empty_thrown_exception_must_have_given_formatted_message(
        final String message
    ) {
        final String expectedMessage = String.format("MSG: %s", message);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonEmpty("", "MSG: %s", message))
            .withMessage(expectedMessage);
    }

    @Test
    public void require_non_empty_thrown_exception_must_have_default_message_if_none_given() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireNonEmpty(""))
            .withMessage("Expected non-empty string");
    }

    @Property
    public void require_extact_length_must_throw_exception_on_incorrect_lenght(
        final int length, final String string
    ) {
        assumeThat(string.length(), is(not(length)));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireExactLength(string, length));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireExactLength(string, length, "test"));
    }

    @Property
    public void require_exact_length_must_return_given_string_if_lenght_matches(
        final String string
    ) {
        final int len = string.length();
        assertThat(requireExactLength(string, len        )).isEqualTo(string);
        assertThat(requireExactLength(string, len, "test")).isEqualTo(string);
    }

    @Property
    public void require_non_empty_thrown_exception_must_have_given_formatted_message(
        final int length, final String string, final String message
    ) {
        assumeThat(string.length(), is(not(length)));

        final String expectedMessage = String.format("MSG: %s", message);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireExactLength(string, length, "MSG: %s", message))
            .withMessage(expectedMessage);
    }

    @Property
    public void require_non_empty_thrown_exception_must_have_default_message_if_none_given(
        final int length, final String string
    ) {
        assumeThat(string.length(), is(not(length)));

        final String expectedMessage = String.format(
            "Expected string of length %d, but '%s' (%d characters) given",
            length, string, string.length()
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> requireExactLength(string, length))
            .withMessage(expectedMessage);
    }

}

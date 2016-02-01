package com.github.agjacome.httpserver.util.contract;

import java.util.Arrays;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeThat;

import static com.github.agjacome.httpserver.util.contract.NullContracts.requireAllNonNull;

@RunWith(Theories.class)
public class NullContractsTest {

    @Theory
    public void require_all_non_null_must_throw_exception_on_any_null(
        @ForAll final Object[ ] values
    ) {
        assumeThat(Arrays.asList(values), hasItem(nullValue()));

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> requireAllNonNull(values));

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> requireAllNonNull(values, "test"));
    }

    @Theory
    public void require_all_non_null_must_given_values_if_none_is_null(
        @ForAll final Object[ ] values
    ) {
        assumeNotNull(values);

        assertThat(requireAllNonNull(values        )).containsExactly(values);
        assertThat(requireAllNonNull(values, "test")).containsExactly(values);
    }

    @Theory
    public void require_all_non_null_thrown_exception_must_have_given_formatted_message(
        @ForAll(sampleSize = 10) final Object[ ] values,
        @ForAll(sampleSize =  2) final String message
    ) {
        assumeThat(Arrays.asList(values), hasItem(nullValue()));

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> requireAllNonNull(values, "MSG: %s", message))
            .withMessage(String.format("MSG: %s", message));
    }

    @Theory
    public void require_all_non_null_thrown_exception_must_have_default_message_if_none_given(
        @ForAll final Object[ ] values
    ) {
        assumeThat(Arrays.asList(values), hasItem(nullValue()));

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> requireAllNonNull(values))
            .withMessage("Expected all values to be non-null, but null value found");
    }

}

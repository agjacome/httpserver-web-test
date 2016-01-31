package com.github.agjacome.httpserver.util.contract;

import java.util.Arrays;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeThat;

import static com.github.agjacome.httpserver.util.contract.NullContracts.requireAllNonNull;

@RunWith(JUnitQuickcheck.class)
public class NullContractsTest {

    @Property
    public <A> void require_all_non_null_must_throw_exception_on_any_null(
        final A[ ] values
    ) {
        assumeThat(Arrays.asList(values), hasItem(nullValue()));

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> requireAllNonNull(values));

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> requireAllNonNull(values, "test"));
    }

    @Property
    public <A> void require_all_non_null_must_given_values_if_none_is_null(
        final A[ ] values
    ) {
        assumeNotNull(values);

        assertThat(requireAllNonNull(values        )).containsExactly(values);
        assertThat(requireAllNonNull(values, "test")).containsExactly(values);
    }

    @Property
    public <A> void require_all_non_null_thrown_exception_must_have_given_formatted_message(
        final A[ ] values, final String message
    ) {
        assumeThat(Arrays.asList(values), hasItem(nullValue()));

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> requireAllNonNull(values, "MSG: %s", message))
            .withMessage(String.format("MSG: %s", message));
    }

    @Property
    public <A> void require_all_non_null_thrown_exception_must_have_default_message_if_none_given(
        final A[ ] values
    ) {
        assumeThat(Arrays.asList(values), hasItem(nullValue()));

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> requireAllNonNull(values))
            .withMessage("Expected all values to be non-null, but null value found");
    }

}

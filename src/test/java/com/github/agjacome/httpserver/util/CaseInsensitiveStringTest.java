package com.github.agjacome.httpserver.util;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.java.lang.Encoded.InCharset;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

@RunWith(JUnitQuickcheck.class)
public class CaseInsensitiveStringTest {

    @Test
    public void uncased_constructor_must_throw_exception_on_null_string() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> uncased(null));
    }

    @Property
    public void get_original_must_return_the_original_string(
        final String string
    ) {
        assertThat(uncased(string).getOriginal()).isEqualTo(string);
    }

    @Property
    public void equals_must_ignore_case(
        @InCharset("US-ASCII") final String string
    ) {
        final CaseInsensitiveString uppercased = uncased(string.toUpperCase());
        final CaseInsensitiveString lowercased = uncased(string.toLowerCase());

        assertThat(uncased(string)).isEqualTo(uppercased);
        assertThat(uncased(string)).isEqualTo(lowercased);

    }

    @Property
    public void hashcode_must_ignore_case(
        @InCharset("US-ASCII") final String string
    ) {
        final CaseInsensitiveString uppercased = uncased(string.toUpperCase());
        final CaseInsensitiveString lowercased = uncased(string.toLowerCase());

        assertThat(uncased(string).hashCode()).isEqualTo(uppercased.hashCode());
        assertThat(uncased(string).hashCode()).isEqualTo(lowercased.hashCode());
    }

    @Property
    public void compare_to_must_ignore_case(
        final String string1, final String string2
    ) {
        assertThat(uncased(string1).compareTo(uncased(string2)))
            .isEqualTo(string1.compareToIgnoreCase(string2));
    }

    @Property
    public void compare_to_must_respect_case_insensitive_equality(
        @InCharset("US-ASCII") final String string
    ) {
        final CaseInsensitiveString uppercased = uncased(string.toUpperCase());
        final CaseInsensitiveString lowercased = uncased(string.toLowerCase());

        assertThat(uncased(string)).isEqualByComparingTo(lowercased);
        assertThat(uncased(string)).isEqualByComparingTo(uppercased);
    }

    @Property
    public void to_string_must_return_original_string_lowercased(
        final String string
    ) {
        assertThat(uncased(string)).hasToString(string.toLowerCase());
    }

    @Test
    public void equals_and_hashcode_contracts_must_be_satisfied() {
        EqualsVerifier.forClass(CaseInsensitiveString.class)
            .suppress(Warning.NULL_FIELDS).verify();
    }

}

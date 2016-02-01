package com.github.agjacome.httpserver.util;

import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.generator.java.lang.Encoded;
import com.pholser.junit.quickcheck.generator.java.lang.Encoded.InCharset;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

@RunWith(Theories.class)
public class CaseInsensitiveStringTest {

    @Test
    public void uncased_constructor_must_throw_exception_on_null_string() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> uncased(null));
    }

    @Theory
    public void get_original_must_return_the_original_string(
        @ForAll final String string
    ) {
        assertThat(uncased(string).getOriginal()).isEqualTo(string);
    }

    @Theory
    public void equals_must_ignore_case(
        @ForAll @From(Encoded.class) @InCharset("US-ASCII") final String string
    ) {
        final CaseInsensitiveString uppercased = uncased(string.toUpperCase());
        final CaseInsensitiveString lowercased = uncased(string.toLowerCase());

        assertThat(uncased(string)).isEqualTo(uppercased);
        assertThat(uncased(string)).isEqualTo(lowercased);

    }

    @Theory
    public void hashcode_must_ignore_case(
        @ForAll @From(Encoded.class) @InCharset("US-ASCII") final String string
    ) {
        final CaseInsensitiveString uppercased = uncased(string.toUpperCase());
        final CaseInsensitiveString lowercased = uncased(string.toLowerCase());

        assertThat(uncased(string).hashCode()).isEqualTo(uppercased.hashCode());
        assertThat(uncased(string).hashCode()).isEqualTo(lowercased.hashCode());
    }

    @Theory
    public void compare_to_must_ignore_case(
        @ForAll final String string1,
        @ForAll final String string2
    ) {
        assertThat(uncased(string1).compareTo(uncased(string2)))
            .isEqualTo(string1.compareToIgnoreCase(string2));
    }

    @Theory
    public void compare_to_must_respect_case_insensitive_equality(
        @ForAll @From(Encoded.class) @InCharset("US-ASCII") final String string
    ) {
        final CaseInsensitiveString uppercased = uncased(string.toUpperCase());
        final CaseInsensitiveString lowercased = uncased(string.toLowerCase());

        assertThat(uncased(string)).isEqualByComparingTo(lowercased);
        assertThat(uncased(string)).isEqualByComparingTo(uppercased);
    }

    @Test
    public void equals_and_hashcode_contracts_must_be_satisfied() {
        EqualsVerifier.forClass(CaseInsensitiveString.class)
            .suppress(Warning.NULL_FIELDS).verify();
    }

    @Theory
    public void to_string_must_return_original_string_lowercased(
        @ForAll final String string
    ) {
        assertThat(uncased(string)).hasToString(string.toLowerCase());
    }

}

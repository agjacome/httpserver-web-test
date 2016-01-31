package com.github.agjacome.httpserver.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.BCrypt;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import nl.jqno.equalsverifier.EqualsVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;

import static nl.jqno.equalsverifier.Warning.NULL_FIELDS;

@RunWith(JUnitQuickcheck.class)
public class BCryptPasswordTest {

    private String hash(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Test
    public void of_must_throw_exception_on_null_password() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> BCryptPassword.of(null));
    }

    @Property
    public void of_must_throw_exception_on_invalid_length(
        final String string
    ) {
        assumeThat(string.length(), is(not(60)));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> BCryptPassword.of(string));
    }

    @Test
    public void encrypt_mus_throw_exception_on_null_password() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> BCryptPassword.encrypt(null));
    }

    @Property
    public void verify_should_return_true_on_valid_password(
        final String password
    ) {
        assertThat(BCryptPassword.encrypt(password).verify(password)).isTrue();
        assertThat(BCryptPassword.of(hash(password)).verify(password)).isTrue();
    }

    @Property
    public void verify_should_return_false_on_invalid_password(
        final String password, final String invalid
    ) {
        assumeThat(password, is(not(invalid)));

        assertThat(BCryptPassword.encrypt(password).verify(invalid)).isFalse();
        assertThat(BCryptPassword.of(hash(password)).verify(invalid)).isFalse();
    }

    @Test
    public void equals_and_hashcode_contracts_must_be_satisfied() {
        EqualsVerifier.forClass(BCryptPassword.class)
            .suppress(NULL_FIELDS).verify();
    }

    @Property
    public void to_string_should_always_return_asterisks(
        final String password
    ) {
        assertThat(BCryptPassword.encrypt(password)).hasToString("***");
        assertThat(BCryptPassword.of(hash(password))).hasToString("***");
    }


}

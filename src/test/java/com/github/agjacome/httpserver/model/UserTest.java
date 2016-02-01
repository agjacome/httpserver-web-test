package com.github.agjacome.httpserver.model;

import java.util.Set;
import java.util.StringJoiner;

import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;
import com.github.agjacome.httpserver.util.PasswordVerifier;
import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.ValuesOf;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.github.agjacome.httpserver.model.User.Role.PAGE_1;
import static com.github.agjacome.httpserver.test.dataset.UserArguments.*;
import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

@RunWith(Theories.class)
public class UserTest {

    @Test
    public void constructor_must_throw_exception_on_null_username() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> new User(null, aPasswordVerifier(), aRoleSet()));
    }

    @Test
    public void constructor_must_throw_exception_on_null_password() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> new User(anUsername(), null, aRoleSet()));
    }

    @Test
    public void constructor_must_throw_exception_on_null_role_set() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> new User(anUsername(), aPasswordVerifier(), null));
    }

    @Theory
    public void get_id_returns_case_insensitive_username(
        @ForAll final String string
    ) {
        final CaseInsensitiveString username = uncased(string);

        final User user = new User(username, aPasswordVerifier(), aRoleSet());
        assertThat(user.getId()).isEqualTo(username);
    }

    @Theory
    public void get_username_must_return_original_cased_string(
        @ForAll final String string
    ) {
        final CaseInsensitiveString username = uncased(string);

        final User user = new User(username, aPasswordVerifier(), aRoleSet());
        assertThat(user.getUsername()).isEqualTo(string);
    }

    @Theory
    public void check_password_must_delegate_to_password_verifier(
        @ForAll final String plainPass,
        @ForAll @ValuesOf final boolean validPass
    ) {
        final PasswordVerifier mockedPass = mock(PasswordVerifier.class);
        final User user = new User(anUsername(), mockedPass, aRoleSet());

        when(mockedPass.verify(plainPass)).thenReturn(validPass);

        assertThat(user.checkPassword(plainPass)).isEqualTo(validPass);

        verify(mockedPass).verify(plainPass);
    }

    @Test
    public void with_password_must_create_new_user_with_given_password() {
        final User newUser = anUser().withPassword(invalidPassword()::equals);

        assertThat(newUser.checkPassword(validPassword())).isFalse();
        assertThat(newUser.checkPassword(invalidPassword())).isTrue();
    }

    @Test
    public void with_password_must_not_mutate_user() {
        final User user = anUser();
        user.withPassword(invalidPassword()::equals);

        assertThat(user.checkPassword(validPassword())).isTrue();
        assertThat(user.checkPassword(invalidPassword())).isFalse();
    }

    @Theory
    public void get_roles_must_return_immutable_set(
        @ForAll final Set<Role> roles
    ) {
        final User user = new User(anUsername(), aPasswordVerifier(), roles);

        assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> user.getRoles().add(PAGE_1));

        assertThatExceptionOfType(UnsupportedOperationException.class)
            .isThrownBy(() -> user.getRoles().remove(PAGE_1));
    }

    @Theory
    public void has_role_must_delegate_on_role_set(
        @ForAll @ValuesOf final Role    role,
        @ForAll @ValuesOf final boolean containsRole
    ) {
        @SuppressWarnings("unchecked")
        final Set<Role> mockedSet = mock(Set.class);
        final User user = new User(anUsername(), aPasswordVerifier(), mockedSet);

        when(mockedSet.contains(role)).thenReturn(containsRole);

        assertThat(user.hasRole(role)).isEqualTo(containsRole);

        verify(mockedSet).contains(role);
    }

    @Test
    public void with_added_role_must_create_new_user_with_given_role_included() {
        final User newUser = anUser().withAddedRole(aNotIncludedRole());
        assertThat(newUser.hasRole(aNotIncludedRole())).isTrue();
    }

    @Test
    public void with_added_role_must_not_mutate_user() {
        final User user = anUser();
        user.withAddedRole(aNotIncludedRole());

        assertThat(user.hasRole(aNotIncludedRole())).isFalse();
    }

    @Test
    public void with_deleted_role_must_create_new_user_with_given_role_deleted() {
        final User newUser = anUser().withDeletedRole(anIncludedRole());
        assertThat(newUser.hasRole(anIncludedRole())).isFalse();
    }

    @Test
    public void with_deleted_role_must_not_mutate_user() {
        final User user = anUser();
        user.withDeletedRole(anIncludedRole());

        assertThat(user.hasRole(anIncludedRole())).isTrue();
    }

    @Test
    public void equals_and_hashcode_contracts_must_be_satisfied() {
        EqualsVerifier.forClass(User.class)
            .suppress(Warning.NULL_FIELDS).verify();
    }

    @Theory
    public void to_string_must_return_string_representation_with_username_and_roles(
        @ForAll final String username, @ForAll final Set<Role> roles
    ) {
        final User user = new User(uncased(username), aPasswordVerifier(), roles);

        final StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (final Role role : roles) joiner.add(role.toString());

        final String expectedToString = String.format(
            "User(username = %s, roles = %s)", username, joiner
        );

        assertThat(user).hasToString(expectedToString);
    }

}

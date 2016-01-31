package com.github.agjacome.httpserver.model;

import java.util.HashSet;
import java.util.Set;

import com.github.agjacome.httpserver.util.CaseInsensitiveString;
import com.github.agjacome.httpserver.util.PasswordVerifier;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public class User implements Entity<CaseInsensitiveString> {

    public static enum Role {
        ADMIN, PAGE_1, PAGE_2, PAGE_3
    }

    private final CaseInsensitiveString username;
    private final PasswordVerifier      password;
    private final Set<Role>             roles;

    public User(
        final CaseInsensitiveString username,
        final PasswordVerifier      password,
        final Set<Role>             roles
    ) throws NullPointerException {
        this.username = requireNonNull(username);
        this.password = requireNonNull(password);
        this.roles    = requireNonNull(roles);
    }

    @Override
    public CaseInsensitiveString getId() {
        return username;
    }

    public String getUsername() {
        return username.getOriginal();
    }

    public boolean checkPassword(final String password) {
        return this.password.verify(password);
    }

    public User withPassword(final PasswordVerifier password) {
        return new User(username, password, roles);
    }

    public Set<Role> getRoles() {
        return unmodifiableSet(roles);
    }

    public boolean hasRole(final Role role) {
        return roles.contains(role);
    }

    public User withAddedRole(final Role role) {
        final Set<Role> roles = new HashSet<>(this.roles);
        roles.add(role);

        return new User(username, password, roles);
    }

    public User withDeletedRole(final Role role) {
        final Set<Role> roles = new HashSet<>(this.roles);
        roles.remove(role);

        return new User(username, password, roles);
    }

    @Override
    public final boolean equals(final Object that) {
        return this == that
            || nonNull(that)
            && that instanceof User
            && username.equals(((User) that).username);
    }

    @Override
    public final int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "User(username = %s, roles = %s)",
            username.getOriginal(),
            roles.stream().map(Role::toString).collect(joining(",", "[", "]"))
        );
    }

}

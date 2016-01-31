package com.github.agjacome.httpserver.util;

import org.mindrot.BCrypt;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.util.contract.StringContracts.requireExactLength;

/**
 * {@linkplain BCryptPassword} is a {@link PasswordVerifier} implementation that
 * uses BCrypt's algorithm internally. It encapsulates JBCrypt's implementation
 * by storing the hashed password as an attribute, and only exposes publicly the
 * interface's {@link #verify(String)} method, for checking if a plaintext
 * password matches with the hashed one.
 */
public final class BCryptPassword implements PasswordVerifier {

    private final String encrypted;

    public static BCryptPassword of(final String encrypted) {
        requireExactLength(requireNonNull(encrypted), 60);

        return new BCryptPassword(encrypted);
    }

    public static BCryptPassword encrypt(final String password) {
        requireNonNull(password);

        return of(BCrypt.hashpw(password, BCrypt.gensalt()));
    }

    private BCryptPassword(final String encrypted) {
        this.encrypted = encrypted;
    }

    @Override
    public boolean verify(final String password) {
        return BCrypt.checkpw(password, encrypted);
    }

    @Override
    public boolean equals(final Object that) {
        return this == that
            || nonNull(that)
            && that instanceof BCryptPassword
            && encrypted.equals(((BCryptPassword) that).encrypted);
    }

    @Override
    public int hashCode() {
        return encrypted.hashCode();
    }

    @Override
    public String toString() {
        return "***";
    }

}

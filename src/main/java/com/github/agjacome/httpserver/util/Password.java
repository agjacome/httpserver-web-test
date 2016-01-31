package com.github.agjacome.httpserver.util;

/**
 * {@linkplain Password} abstracts any password verification and hashing
 * strategy. Internal hashing and/or secure storage of passwords in memory is
 * implementation-dependent.
 */
@FunctionalInterface
public interface Password {

    public boolean verify(final String password);

}

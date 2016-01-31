package com.github.agjacome.httpserver.util;

import java.util.Collection;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * {@linkplain CaseInsensitiveString} is a naive {@link String} wrapper that
 * allows it to be used in any {@link Collection} as if case was not relevant in
 * comparisons (by correctly overriding {@link #equals(Object)} and
 * {@link #hashCode()}). Original cased string can be retrieved through its
 * {@link #getOriginal()} method.
 *
 * <p>
 * <strong>Note:</strong> This implementation does not correctly perform Unicode
 * case-insensitive matching, as it relies upon
 * {@link String#equalsIgnoreCase(String)} and {@link String#toLowerCase()}. If
 * full Unicode support is required, consider using
 * <a href="http://site.icu-project.org/">ICU</a> and its <a href=
 * "https://ssl.icu-project.org/apiref/icu4j/com/ibm/icu/util/CaseInsensitiveString.html">
 * CaseInsensitiveString</a> implementation instead.>
 * </p>
 */
public final class CaseInsensitiveString implements Comparable<CaseInsensitiveString> {

    private final String string;

    public static CaseInsensitiveString uncased(final String string) {
        requireNonNull(string);
        return new CaseInsensitiveString(string);
    }

    private CaseInsensitiveString(final String string) {
        this.string = string;
    }

    public String getOriginal() {
        return string;
    }

    @Override
    public int compareTo(final CaseInsensitiveString that) {
        return string.compareToIgnoreCase(that.string);
    }

    @Override
    public boolean equals(final Object that) {
        return this == that
            || nonNull(that)
            && that instanceof CaseInsensitiveString
            && string.equalsIgnoreCase(((CaseInsensitiveString) that).string);
    }

    @Override
    public int hashCode() {
        return string.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return string.toLowerCase();
    }

}

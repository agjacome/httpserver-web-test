package com.github.agjacome.httpserver.server.http;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.agjacome.httpserver.util.URIs;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public final class HttpQueryParameter implements HttpRequestMatcher, Iterable<String> {

    private final String       key;
    private final List<String> values;

    public static Map<String, HttpQueryParameter> parseQueryParams(
        final String rawQuery
    ) {
        final Map<String, HttpQueryParameter> params = new LinkedHashMap<>();

        URIs.parseParams(rawQuery).forEach(
            (k, vs) -> params.put(k, new HttpQueryParameter(k, vs))
        );

        return params;
    }

    public HttpQueryParameter(final String key, final String ... values) {
        this(key, Arrays.asList(values));
    }

    public HttpQueryParameter(final String key, final List<String> values) {
        this.key    = requireNonNull(key);
        this.values = requireNonNull(values);
    }

    @Override
    public boolean matches(final HttpRequest request) {
        return request.getQueryParameter(key)
              .filter(that -> that.values.containsAll(this.values))
              .isPresent();
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    public String getKey() {
        return key;
    }

    public Optional<String> getFirst() {
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(0));
    }

    public List<String> getValues() {
        return unmodifiableList(values);
    }

    @Override
    public boolean equals(final Object that) {
        return this == that
            || nonNull(that)
            && that instanceof HttpQueryParameter
            && key.equals(((HttpQueryParameter) that).key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        final String encodedKey = URIs.encodeUTF8toURL(key);
        return values.stream().map(
            value -> value.isEmpty()
                   ? encodedKey
                   : encodedKey + "=" + URIs.encodeUTF8toURL(value)
        ).collect(joining("&"));
    }

}

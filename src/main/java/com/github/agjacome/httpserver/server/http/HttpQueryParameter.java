package com.github.agjacome.httpserver.server.http;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.agjacome.httpserver.util.URIs;
import com.sun.net.httpserver.HttpExchange;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public final class HttpQueryParameter implements HttpExchangeMatcher, Iterable<String> {

    private final String       key;
    private final List<String> values;

    public static Map<String, HttpQueryParameter> parseQueryParams(
        final HttpExchange exchange
    ) {
        final Map<String, HttpQueryParameter> params = new LinkedHashMap<>();

        URIs.parseParams(requireNonNull(exchange).getRequestURI()).forEach(
            (k, vs) -> params.put(k, new HttpQueryParameter(k, vs))
        );

        return params;
    }

    public HttpQueryParameter(final String key, final List<String> values) {
        this.key    = requireNonNull(key);
        this.values = requireNonNull(values);
    }

    @Override
    public boolean matches(final HttpExchange exchange) {
        return parseQueryParams(exchange).containsKey(key);
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    public String getKey() {
        return key;
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

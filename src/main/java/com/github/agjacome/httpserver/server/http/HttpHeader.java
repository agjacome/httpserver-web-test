package com.github.agjacome.httpserver.server.http;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.github.agjacome.httpserver.util.CaseInsensitiveString;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;
import static com.github.agjacome.httpserver.util.contract.NullContracts.requireAllNonNull;

public final class HttpHeader implements HttpExchangeMatcher, Iterable<String> {

    private final CaseInsensitiveString key;
    private final List<String>          values;

    public static Map<CaseInsensitiveString, HttpHeader> parseHeaders(
        final HttpExchange exchange,
        final Function<HttpExchange, Map<String, List<String>>> headersGetter
    ) {
        requireAllNonNull(exchange, headersGetter);

        final Map<CaseInsensitiveString, HttpHeader> headers = new LinkedHashMap<>();

        headersGetter.apply(exchange).forEach((key, values) -> {
            final CaseInsensitiveString headerKey = uncased(key);
            headers.put(headerKey, new HttpHeader(headerKey, values));
        });

        return headers;
    }

    public HttpHeader(
        final CaseInsensitiveString key, final List<String> values
    ) {
        this.key    = requireNonNull(key);
        this.values = requireNonNull(values);
    }

    @Override
    public boolean matches(final HttpExchange exchange) {
        final String  header  = key.getOriginal();
        final Headers headers = exchange.getRequestHeaders();

        return headers.containsKey(header.toLowerCase())
            || headers.containsKey(header.toUpperCase());
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    public CaseInsensitiveString getKey() {
        return key;
    }

    public List<String> getValues() {
        return unmodifiableList(values);
    }

    @Override
    public boolean equals(final Object that) {
        return this == that
            || nonNull(that)
            && that instanceof HttpHeader
            && key.equals(((HttpHeader) that).key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "%s: %s",
            key.getOriginal(),
            values.stream().collect(joining(","))
        );
    }

}
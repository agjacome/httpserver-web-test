package com.github.agjacome.httpserver.server.http;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.agjacome.httpserver.util.CaseInsensitiveString;
import com.sun.net.httpserver.HttpExchange;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.NO_CONTENT;

public final class HttpExchangeResponseBuilder implements HttpResponseBuilder {

    private long length;
    private HttpStatusCode code;
    private Map<CaseInsensitiveString, HttpHeader> headers;

    private final HttpExchange exchange;

    public HttpExchangeResponseBuilder(final HttpExchange exchange) {
        this.exchange = requireNonNull(exchange);

        this.length  = -1;
        this.code    = NO_CONTENT;
        this.headers = new LinkedHashMap<>();
    }

    @Override
    public HttpResponseBuilder withStatusCode(final HttpStatusCode code) {
        this.code = requireNonNull(code);
        return this;
    }

    @Override
    public HttpResponseBuilder withContentLength(final long length) {
        this.length = length;
        return this;
    }

    @Override
    public HttpResponseBuilder withHeader(final HttpHeader header) {
        this.headers.put(header.getKey(), header);
        return this;
    }

    @Override
    public HttpResponse build() throws IOException {
        exchange.sendResponseHeaders(code.getCode(), length);

        headers.values().forEach(header -> {
            final String       key    = header.getKey().getOriginal();
            final List<String> values = header.getValues();

            exchange.getResponseHeaders().put(key, values);
        });

        return new HttpExchangeResponseAdapter(exchange);
    }

}

package com.github.agjacome.httpserver.server.http;

import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

import com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;
import com.sun.net.httpserver.HttpExchange;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.server.http.HttpHeader.parseHeaders;
import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public class HttpExchangeResponseAdapter implements HttpResponse {

    private final HttpExchange                           exchange;
    private final Map<CaseInsensitiveString, HttpHeader> headers;

    HttpExchangeResponseAdapter(final HttpExchange exchange) {
        this.exchange = requireNonNull(exchange);
        this.headers  = parseHeaders(exchange.getResponseHeaders());
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return StandardHttpStatusCode.ofCode(exchange.getResponseCode()).get();
    }

    @Override
    public long getContentLength() {
        return Long.parseLong(
            headers.get(uncased("Content-Length")).getValues().get(0)
        );
    }

    @Override
    public Optional<HttpHeader> getHeader(final CaseInsensitiveString key) {
        return Optional.ofNullable(headers.get(key));
    }

    @Override
    public OutputStream getBodyOutputStream() {
        return exchange.getResponseBody();
    }

    public HttpExchange getExchange() {
        return exchange;
    }

}

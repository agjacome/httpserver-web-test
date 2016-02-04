package com.github.agjacome.httpserver.server.http;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

import com.github.agjacome.httpserver.server.http.HttpMethod.StandardHttpMethod;
import com.github.agjacome.httpserver.server.http.HttpVersion.StandardHttpVersion;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;
import com.sun.net.httpserver.HttpExchange;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.server.http.HttpHeader.parseHeaders;
import static com.github.agjacome.httpserver.server.http.HttpQueryParameter.parseQueryParams;

public class HttpExchangeRequestAdapter implements HttpRequest {

    private final HttpExchange exchange;

    private final Map<String, HttpQueryParameter>        params;
    private final Map<CaseInsensitiveString, HttpHeader> headers;

    public HttpExchangeRequestAdapter(final HttpExchange exchange) {
        this.exchange = requireNonNull(exchange);

        this.params   = parseQueryParams(exchange);
        this.headers  = parseHeaders(exchange, HttpExchange::getRequestHeaders);
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return exchange.getRemoteAddress();
    }

    @Override
    public HttpMethod getMethod() {
        return StandardHttpMethod
              .of(exchange.getRequestMethod())
              .orElse(() -> exchange.getRequestMethod());
    }

    @Override
    public URI getURI() {
        return exchange.getRequestURI();
    }

    @Override
    public String getPath() {
        final String base = exchange.getHttpContext().getPath();
        final String path = getURI().getPath().substring(
            base.equals("/") ? 0 : base.length()
        );

        return path.isEmpty() ? "/" : path;
    }

    @Override
    public Optional<HttpQueryParameter> getQueryParameter(final String key) {
        return Optional.ofNullable(params.get(key));
    }

    @Override
    public HttpVersion getVersion() {
        return StandardHttpVersion
              .of(exchange.getProtocol())
              .orElse(HttpVersion.parse(exchange.getProtocol()));
    }

    @Override
    public Optional<HttpHeader> getHeader(final CaseInsensitiveString key) {
        return Optional.ofNullable(headers.get(key));
    }

    @Override
    public InputStream getBodyInputStream() {
        return exchange.getRequestBody();
    }

    public HttpExchange getExchange() {
        return exchange;
    }

}

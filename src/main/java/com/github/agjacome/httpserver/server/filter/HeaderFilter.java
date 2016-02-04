package com.github.agjacome.httpserver.server.filter;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpHeader;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public final class HeaderFilter implements ServerRequestFilter {

    private final HttpHeader header;

    public static ServerRequestFilter header(
        final String key, final String ... values
    ) {
        return header(new HttpHeader(uncased(key), values));
    }

    public static ServerRequestFilter header(final HttpHeader header) {
        return new HeaderFilter(header);
    }

    public HeaderFilter(final HttpHeader header) {
        this.header = requireNonNull(header);
    }

    @Override
    public boolean matches(final ServerRequest request) {
        return header.matches(request.getHttpRequest());
    }

}

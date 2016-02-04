package com.github.agjacome.httpserver.server.filter;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpMethod;

import static java.util.Objects.requireNonNull;

public final class MethodFilter implements ServerRequestFilter {

    private final HttpMethod method;

    public static ServerRequestFilter method(final HttpMethod method) {
        return new MethodFilter(method);
    }

    public MethodFilter(final HttpMethod method) {
        this.method = requireNonNull(method);
    }

    @Override
    public boolean matches(final ServerRequest request) {
        return method.matches(request.getHttpRequest());
    }

}

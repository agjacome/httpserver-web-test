package com.github.agjacome.httpserver.server.filter;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpVersion;

import static java.util.Objects.requireNonNull;

public final class VersionFilter implements ServerRequestFilter {

    private final HttpVersion version;

    public static ServerRequestFilter version(final HttpVersion version) {
        return new VersionFilter(version);
    }

    public VersionFilter(final HttpVersion version) {
        this.version = requireNonNull(version);
    }

    @Override
    public boolean matches(final ServerRequest request) {
        return version.matches(request.getHttpRequest());
    }

}

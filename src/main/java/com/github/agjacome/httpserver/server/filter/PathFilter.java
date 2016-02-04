package com.github.agjacome.httpserver.server.filter;

import java.util.regex.Pattern;

import com.github.agjacome.httpserver.server.ServerRequest;

import static java.util.Objects.requireNonNull;

public final class PathFilter implements ServerRequestFilter {

    private final Pattern pathPattern;

    public static ServerRequestFilter path(final String pathPattern) {
        return path(Pattern.compile(pathPattern));
    }

    public static ServerRequestFilter path(final Pattern pathPattern) {
        return new PathFilter(pathPattern);
    }

    public PathFilter(final Pattern pathPattern) {
        this.pathPattern = requireNonNull(pathPattern);
    }

    @Override
    public boolean matches(final ServerRequest request) {
        final String path = request.getHttpRequest().getPath();
        return pathPattern.matcher(path).matches();
    }

}

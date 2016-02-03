package com.github.agjacome.httpserver.server.http;

import java.util.function.Predicate;

@FunctionalInterface
public interface HttpRequestMatcher extends Predicate<HttpRequest> {

    public boolean matches(final HttpRequest request);

    @Override
    public default boolean test(final HttpRequest request) {
        return matches(request);
    }

}

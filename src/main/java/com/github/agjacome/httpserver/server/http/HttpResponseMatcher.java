package com.github.agjacome.httpserver.server.http;

import java.util.function.Predicate;

@FunctionalInterface
public interface HttpResponseMatcher extends Predicate<HttpResponse> {

    public boolean matches(final HttpResponse response);

    @Override
    public default boolean test(final HttpResponse response) {
        return matches(response);
    }

}

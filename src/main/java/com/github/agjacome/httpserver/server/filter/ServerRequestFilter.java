package com.github.agjacome.httpserver.server.filter;

import java.util.function.Predicate;

import com.github.agjacome.httpserver.server.ServerRequest;

import rx.functions.Func1;

@FunctionalInterface
public interface ServerRequestFilter extends Predicate<ServerRequest>, Func1<ServerRequest, Boolean> {

    public boolean matches(final ServerRequest request);

    @Override
    public default boolean test(final ServerRequest request) {
        return matches(request);
    }

    @Override
    default Boolean call(final ServerRequest request) {
        return matches(request);
    }

    public default ServerRequestFilter or(final ServerRequestFilter that) {
        return req -> this.matches(req) || that.matches(req);
    }

    public default ServerRequestFilter and(final ServerRequestFilter that) {
        return req -> this.matches(req) && that.matches(req);
    }

}

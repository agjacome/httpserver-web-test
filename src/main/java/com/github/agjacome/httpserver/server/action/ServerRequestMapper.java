package com.github.agjacome.httpserver.server.action;

import java.util.function.Function;

import com.github.agjacome.httpserver.server.ServerRequest;

import rx.functions.Func1;

@FunctionalInterface
public interface ServerRequestMapper<A> extends Function<ServerRequest, A>, Func1<ServerRequest, A> {

    public A map(final ServerRequest request);

    @Override
    public default A apply(final ServerRequest request) {
        return map(request);
    }

    @Override
    public default A call(final ServerRequest request) {
        return map(request);
    }

}

package com.github.agjacome.httpserver.server.action;

import java.util.function.Consumer;

import com.github.agjacome.httpserver.server.ServerRequest;

import rx.functions.Action1;

@FunctionalInterface
public interface ServerRequestEffect extends Consumer<ServerRequest>, Action1<ServerRequest> {

    public void effect(final ServerRequest request);

    @Override
    public default void accept(final ServerRequest request) {
        effect(request);
    }

    @Override
    public default void call(final ServerRequest request) {
        effect(request);
    }

}

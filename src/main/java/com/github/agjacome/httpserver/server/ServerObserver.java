package com.github.agjacome.httpserver.server;

@FunctionalInterface
public interface ServerObserver {

    public void handleRequest(final ServerRequest request);

}

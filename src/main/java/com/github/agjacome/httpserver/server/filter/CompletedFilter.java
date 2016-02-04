package com.github.agjacome.httpserver.server.filter;

import com.github.agjacome.httpserver.server.ServerRequest;

public final class CompletedFilter implements ServerRequestFilter {

    public static final ServerRequestFilter   completed = req ->  req.isCompleted();
    public static final ServerRequestFilter uncompleted = req -> !req.isCompleted();

    @Override
    public boolean matches(final ServerRequest request) {
        return request.isCompleted();
    }

}

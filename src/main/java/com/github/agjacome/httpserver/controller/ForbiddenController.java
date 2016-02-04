package com.github.agjacome.httpserver.controller;

import com.github.agjacome.httpserver.server.ServerRequest;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.FORBIDDEN;

public final class ForbiddenController extends Controller {

    public static final Controller forbidden = new ForbiddenController();

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        request.getHttpResponseBuilder().withStatusCode(FORBIDDEN).build();
        request.complete();
    }

}

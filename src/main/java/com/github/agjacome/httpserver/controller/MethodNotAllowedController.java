package com.github.agjacome.httpserver.controller;

import com.github.agjacome.httpserver.server.ServerRequest;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.METHOD_NOT_ALLOWED;

public final class MethodNotAllowedController extends Controller {

    public static final Controller methodNotAllowed = new MethodNotAllowedController();

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        request.getHttpResponseBuilder().withStatusCode(METHOD_NOT_ALLOWED).build();
        request.complete();
    }

}

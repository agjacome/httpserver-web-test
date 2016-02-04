package com.github.agjacome.httpserver.controller;

import com.github.agjacome.httpserver.server.ServerRequest;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.NOT_FOUND;

public final class NotFoundController extends Controller {

    public static final Controller notFound = new NotFoundController();

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        request.getHttpResponseBuilder().withStatusCode(NOT_FOUND).build();
        request.complete();
    }

}

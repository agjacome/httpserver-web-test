package com.github.agjacome.httpserver.controller;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpResponse;
import com.github.agjacome.httpserver.view.View;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.NOT_FOUND;

public final class NotFoundController extends ViewController {

    public static Controller notFoundController(final View view) {
        return new NotFoundController(view);
    }

    public NotFoundController(final View view) {
        super(view);
    }

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        final HttpResponse response = request.getHttpResponseBuilder()
            .withStatusCode(NOT_FOUND)
            .withHeader(view.getContentType())
            .withContentLength(view.getContentLength())
            .build();

        view.render(response.getBodyOutputStream());
        request.complete();
    }

}

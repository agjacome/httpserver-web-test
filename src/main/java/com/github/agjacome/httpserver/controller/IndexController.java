package com.github.agjacome.httpserver.controller;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpResponse;
import com.github.agjacome.httpserver.view.View;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.OK;

public class IndexController extends Controller {

    private final View view;

    public static Controller indexController(final View view) {
        return new IndexController(view);
    }

    public IndexController(final View view) {
        this.view = requireNonNull(view);
    }

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        final HttpResponse response = request.getHttpResponseBuilder()
            .withStatusCode(OK)
            .withHeader(view.getContentType())
            .withContentLength(view.getContentLength())
            .build();

        view.render(response.getBodyOutputStream());

        request.complete();
    }

}

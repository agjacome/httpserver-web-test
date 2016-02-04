package com.github.agjacome.httpserver.controller;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpResponse;
import com.github.agjacome.httpserver.view.View;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.OK;

public class LoginController extends Controller {

    private final View loginView;

    public static Controller loginControler(final View loginView) {
        return new LoginController(loginView);
    }

    public LoginController(final View loginView) {
        this.loginView = requireNonNull(loginView);
    }

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        final HttpResponse response = request.getHttpResponseBuilder()
            .withStatusCode(OK)
            .withContentLength(loginView.getContentLength())
            .withHeader(loginView.getContentType())
            .build();

        loginView.writeView(response.getBodyOutputStream());
        request.complete();
    }

}

package com.github.agjacome.httpserver.controller;

import java.io.IOException;

import com.github.agjacome.httpserver.model.repository.SessionRepository;
import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.server.http.HttpRequest;
import com.github.agjacome.httpserver.server.http.HttpResponseBuilder;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.FOUND;

public final class LogoutController extends SessionController {

    public static Controller logoutController(final SessionRepository sessions) {
        return new LogoutController(sessions);
    }

    public LogoutController(final SessionRepository sessions) {
        super(sessions);
    }

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        final HttpRequest req = request.getHttpRequest();

        final HttpResponseBuilder response = request.getHttpResponseBuilder();
        getSession(req).ifPresent(s -> response.withHeader(destroySession(s)));

        response.withHeader(getLocation(req)).withStatusCode(FOUND).build();
        request.complete();
    }

    private HttpHeader getLocation(final HttpRequest req) throws IOException {
        final String location = getFirstParamFor("redirect", req).orElse("");
        return header("Location", req.getContextPath() + "/" + location);
    }

}

package com.github.agjacome.httpserver.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.github.agjacome.httpserver.model.Session;
import com.github.agjacome.httpserver.model.repository.SessionRepository;
import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.server.http.HttpRequest;
import com.github.agjacome.httpserver.server.http.HttpResponse;
import com.github.agjacome.httpserver.server.http.HttpResponseBuilder;
import com.github.agjacome.httpserver.view.View;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.FOUND;
import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.OK;

public final class LoginGetController extends SessionController {

    private final View view;

    public static Controller loginGetController(
        final View view, final SessionRepository sessions
    ) {
        return new LoginGetController(view, sessions);
    }

    public LoginGetController(
        final View view, final SessionRepository sessions
    ) {
        super(sessions);
        this.view = requireNonNull(view);
    }

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        final Optional<Session> session = getSession(request.getHttpRequest());

        if (session.isPresent())
            onSessionIsPresent(session.get(), request);
        else
            onSessionNotPresent(request);

        request.complete();
    }

    private void onSessionIsPresent(
        final Session       session,
        final ServerRequest req
    ) throws IOException {
        final HttpResponseBuilder response = req.getHttpResponseBuilder();

        if (updateSession(session).isPresent()) {
            response.withStatusCode(FOUND);
            response.withHeader(getLocation(req.getHttpRequest()));
        } else {
            response.withStatusCode(FOUND);
            response.withHeader(destroySession());
            response.withHeader(header("Location", req.getHttpRequest().getURI().toString()));
        }

        response.build();
    }

    private void onSessionNotPresent(
        final ServerRequest req
    ) throws Exception {
        final Map<String, String> values = getViewValues(req.getHttpRequest());

        final HttpResponse response = req.getHttpResponseBuilder()
            .withStatusCode(OK)
            .withHeader(view.getContentType())
            .withContentLength(view.getContentLength(values))
            .build();

        view.render(values, response.getBodyOutputStream());
    }


    private Map<String, String> getViewValues(final HttpRequest request) {
        return getFirstParamFor("redirect", request)
              .map(p -> singletonMap("redirect", p))
              .orElse(emptyMap());
    }

    private HttpHeader getLocation(final HttpRequest req) throws IOException {
        final String location = getFirstParamFor("redirect", req).orElse("");
        return header("Location", req.getContextPath() + "/" + location);
    }

}

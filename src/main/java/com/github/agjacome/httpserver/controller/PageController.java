package com.github.agjacome.httpserver.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.github.agjacome.httpserver.model.Session;
import com.github.agjacome.httpserver.model.repository.SessionRepository;
import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpResponse;
import com.github.agjacome.httpserver.server.http.HttpResponseBuilder;
import com.github.agjacome.httpserver.view.View;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.FORBIDDEN;
import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.FOUND;
import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.OK;

public final class PageController extends SessionController {

    private final View view;

    public static Controller pageController(
        final View view, final SessionRepository sessions
    ) {
        return new PageController(view, sessions);
    }

    public PageController(final View view, final SessionRepository sessions) {
        super(sessions);
        this.view = requireNonNull(view);
    }

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        final Optional<Session> session = getSession(request.getHttpRequest());

        if (!session.isPresent())
            onSessionNotPresent(request);
        else if (view.isAccessibleBy(session.get().getUser()))
            onViewIsAccessible(session.get(), request);
        else
            onViewNotAccessible(request);

        request.complete();
    }

    private void onViewNotAccessible(
        final ServerRequest request
    ) throws IOException {
        final String body = "You are not allowed to access the requested page";

        final HttpResponse response = request.getHttpResponseBuilder()
               .withContentLength(body.getBytes().length)
               .withStatusCode(FORBIDDEN)
               .build();

        try (final OutputStream stream = response.getBodyOutputStream()) {
            stream.write(body.getBytes());
            stream.flush();
        }
    }

    private void onViewIsAccessible(
        final Session session, final ServerRequest request
    ) throws Exception {
        final Map<String, String> values = Collections.singletonMap(
            "username", session.getUser().getUsername()
        );

        final HttpResponse response = request.getHttpResponseBuilder()
            .withContentLength(view.getContentLength(values))
            .withStatusCode(OK)
            .build();

        view.render(values, response.getBodyOutputStream());
    }

    private void onSessionNotPresent(
        final ServerRequest request
    ) throws IOException {
        final HttpResponseBuilder response = request.getHttpResponseBuilder();

        response.withStatusCode(FOUND);
        response.withHeader(header("Location", String.format(
            "%s/login?redirect=%s",
            request.getHttpRequest().getContextPath(),
            request.getHttpRequest().getPath().substring(1)
        )));

        response.build();
    }

}

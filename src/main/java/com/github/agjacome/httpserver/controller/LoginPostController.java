package com.github.agjacome.httpserver.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.github.agjacome.httpserver.model.Session;
import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.model.repository.SessionRepository;
import com.github.agjacome.httpserver.model.repository.UserRepository;
import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.server.http.HttpQueryParameter;
import com.github.agjacome.httpserver.server.http.HttpRequest;
import com.github.agjacome.httpserver.server.http.HttpResponseBuilder;

import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.FOUND;
import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public class LoginPostController extends AuthorizationController {

    private final UserRepository users;
    private final Duration       sessionDuration;

    public static Controller loginPostController(
        final UserRepository    users,
        final SessionRepository sessions,
        final Duration          sessionDuration
    ) {
        return new LoginPostController(users, sessions, sessionDuration);
    }

    public LoginPostController(
        final UserRepository    users,
        final SessionRepository sessions,
        final Duration          sessionDuration
    ) {
        super(sessions);

        this.users           = requireNonNull(users);
        this.sessionDuration = requireNonNull(sessionDuration);
    }

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        final HttpRequest req = request.getHttpRequest();

        final Map<String, HttpQueryParameter> params  = parsePostParams(req);
        final HttpHeader redirect = header(
            "Location", getRedirectLocation(req, params)
        );

        final Optional<Session> session = getSession(req);
        final HttpResponseBuilder response = session.isPresent()
                ? onSessionIsPresent(session.get(), request, redirect)
                : onValidUser(getValidatedUser(params), request, redirect);

        response.build();
        request.complete();
    }

    private HttpResponseBuilder onSessionIsPresent(
        final Session       session,
        final ServerRequest request,
        final HttpHeader    redirect
    ) throws IOException {
        final HttpResponseBuilder response = request.getHttpResponseBuilder();
        final Optional<Session>   updated  = updateSession(session);

        if (updated.isPresent()) {
            response.withStatusCode(FOUND);
            response.withHeader(redirect);
        } else {
             response.withStatusCode(FOUND);
             response.withHeader(unsetSessionHeader());
             response.withHeader(header("Location", request.getHttpRequest().getURI().toString()));
        }

        return response;
    }

    private HttpResponseBuilder onValidUser(
        final Optional<User> user,
        final ServerRequest  request,
        final HttpHeader     redirect
    ) throws IOException {
        final HttpResponseBuilder response = request.getHttpResponseBuilder();

        if (user.isPresent()) {
           response.withStatusCode(FOUND);
           response.withHeader(redirect);
           response.withHeader(session(user.get()));
        } else {
            response.withStatusCode(FOUND);
            response.withHeader(header("Location", request.getHttpRequest().getURI().toString()));
        }

        return response;
    }

    private HttpHeader session(final User user) {
        return createSessionHeader(new Session(
            randomUUID(), user, now(), sessionDuration
        ));
    }

    private Optional<User> getValidatedUser(
        final Map<String, HttpQueryParameter> params
    ) {
        final Optional<String> username = ofNullable(params.get("username"))
                .filter(p -> !p.getValues().isEmpty())
                .map(p -> p.getValues().get(0));

        final Optional<String> password = ofNullable(params.get("password"))
                .filter(p -> !p.getValues().isEmpty())
                .map(p -> p.getValues().get(0));

        if (!username.isPresent() || !password.isPresent())
            return Optional.empty();

        return users.get(uncased(username.get()))
              .filter(u -> u.checkPassword(password.get()));
    }

    private String getRedirectLocation(
        final HttpRequest request,
        final Map<String, HttpQueryParameter> params
    ) throws IOException {
        final String redirect = ofNullable(params.get("redirect"))
             .filter(p -> !p.getValues().isEmpty())
             .map(p -> p.getValues().get(0))
             .orElse("");

        return request.getContextPath() + "/" + redirect;
    }

    private Map<String, HttpQueryParameter> parsePostParams(
        final HttpRequest request
    ) throws IOException {
        final HttpHeader urlEncoded = header(
            "Content-Type", "application/x-www-form-urlencoded"
        );

        if (!urlEncoded.matches(request)) return Collections.emptyMap();

        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getBodyInputStream())
        )) {
            final String firstLine = reader.readLine();
            return HttpQueryParameter.parseQueryParams(firstLine);
        }
    }

}

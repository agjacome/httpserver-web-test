package com.github.agjacome.httpserver.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.agjacome.httpserver.model.Session;
import com.github.agjacome.httpserver.model.repository.SessionRepository;
import com.github.agjacome.httpserver.model.repository.exception.RepositoryException;
import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.server.http.HttpRequest;

import static java.time.Instant.EPOCH;
import static java.time.Instant.now;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

abstract class AuthorizationController extends Controller {

    private final SessionRepository sessions;

    protected AuthorizationController(final SessionRepository sessions) {
        this.sessions = requireNonNull(sessions);
    }

    protected Optional<Session> updateSession(final Session session) {
        try {

            if (session.hasExpired()) {
                sessions.delete(session);
                return Optional.empty();
            }

            return Optional.of(sessions.update(session.withInstant(now())));

        } catch (final RepositoryException re) {
            return Optional.empty();
        }
    }

    protected HttpHeader createSessionHeader(final Session session) {
        final String uuid    = session.getId().toString();
        final String expires = session.getInstant().plus(session.getDuration())
                              .atOffset(UTC).format(RFC_1123_DATE_TIME);

        return header(
            "Set-Cookie",
            String.format("session=%s; expires=%s", uuid, expires)
        );
    }

    protected HttpHeader unsetSessionHeader() {
        final String expires = EPOCH.atOffset(UTC).format(RFC_1123_DATE_TIME);

        return header(
            "Set-Cookie",
            String.format("session=%s; expires=%s", "null", expires)
        );
    }

    protected Optional<Session> getSession(final HttpRequest request) {
        return request.getHeader(uncased("Cookie")).flatMap(this::getSession);
    }

    protected Optional<Session> getSession(final HttpHeader cookie) {
        final List<String> values = cookie.getValues();
        if (values.isEmpty()) return Optional.empty();

        return getSessionValue(values).flatMap(sessions::get);
    }


    private Optional<UUID> getSessionValue(final List<String> values) {
        // TODO: refactor
        for (final String value : values) {
            final String[] components = value.split(";");

            for (final String component : components) {
                final String[] keyValue = component.trim().split("=");

                    if (keyValue.length == 2 && keyValue[0].trim().equals("session"))
                        return parseUUID(keyValue[0]);
            }
        }

        return Optional.empty();
    }

    private Optional<UUID> parseUUID(final String uuid) {
        try {
            return Optional.of(UUID.fromString(uuid));
        } catch (final IllegalArgumentException iae) {
            return Optional.empty();
        }
    }

}

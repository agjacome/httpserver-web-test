package com.github.agjacome.httpserver.model.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.github.agjacome.httpserver.model.Session;
import com.github.agjacome.httpserver.model.User;

public interface SessionRepository extends Repository<UUID, Session> {

    public default ResultBuilder<Session, List<Session>> searchByUser(
        final User user
    ) {
        return search(s -> s.getUser().equals(user));
    }

    public default ResultBuilder<Session, List<Session>> searchByInstant(
        final Instant instant
    ) {
        return search(s -> s.getInstant().equals(instant));
    }

}

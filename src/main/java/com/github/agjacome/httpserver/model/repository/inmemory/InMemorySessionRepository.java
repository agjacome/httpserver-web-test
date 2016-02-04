package com.github.agjacome.httpserver.model.repository.inmemory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.github.agjacome.httpserver.model.Session;
import com.github.agjacome.httpserver.model.repository.SessionRepository;

public class InMemorySessionRepository extends InMemoryRepository<UUID, Session> implements SessionRepository {

    public static SessionRepository empty() {
        return of(Collections.emptyList());
    }

    public static SessionRepository of(final Session ... sessions) {
        return of(Arrays.asList(sessions));
    }

    public static SessionRepository of(final Collection<Session> sessions) {
        return new InMemorySessionRepository(new LinkedHashSet<>(sessions));
    }

    private InMemorySessionRepository(final Set<Session> sessions) {
        sessions.forEach(this::create);
    }

}

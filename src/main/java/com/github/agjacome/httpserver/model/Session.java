package com.github.agjacome.httpserver.model;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class Session implements Entity<UUID> {

    private final UUID     uuid;
    private final User     user;
    private final Instant  instant;
    private final Duration duration;

    public Session(
        final UUID     uuid,
        final User     user,
        final Instant  instant,
        final Duration duration
    ) {
        this.uuid     = requireNonNull(uuid);
        this.user     = requireNonNull(user);
        this.instant  = requireNonNull(instant);
        this.duration = requireNonNull(duration);
    }

    @Override
    public UUID getId() {
        return uuid;
    }

    public User getUser() {
        return user;
    }

    public Instant getInstant() {
        return instant;
    }

    public Session withInstant(final Instant instant) {
        return new Session(uuid, user, instant, duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public Session withDuration(final Duration duration) {
        return new Session(uuid, user, instant, duration);
    }

    public boolean hasExpired() {
        return instant.plus(duration).isBefore(now());
    }

    @Override
    public boolean equals(final Object that) {
        return this == that
            || nonNull(that)
            && that instanceof Session
            && uuid.equals(((Session) that).uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
            "Session(uuid = %s, user = %s, instant = %d, duration = %ds)",
            uuid.toString(),
            user.toString(),
            instant.getEpochSecond(),
            duration.getSeconds()
        );
    }

}

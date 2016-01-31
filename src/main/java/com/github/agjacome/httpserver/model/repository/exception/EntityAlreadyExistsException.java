package com.github.agjacome.httpserver.model.repository.exception;

import com.github.agjacome.httpserver.model.Entity;

import static java.util.Objects.requireNonNull;

public class EntityAlreadyExistsException extends RepositoryException {

    private static final long serialVersionUID = 1L;

    private final Entity<?> entity;

    public EntityAlreadyExistsException(final Entity<?> entity) {
        super();
        this.entity = requireNonNull(entity);
    }

    public EntityAlreadyExistsException(
        final Entity<?> entity, final String message
    ) {
        super(message);
        this.entity = requireNonNull(entity);
    }

    public EntityAlreadyExistsException(
        final Entity<?> entity, final Throwable cause
    ) {
        super(cause);
        this.entity = requireNonNull(entity);
    }

    public EntityAlreadyExistsException(
        final Entity<?> entity, final String message, final Throwable cause
    ) {
        super(message, cause);
        this.entity = requireNonNull(entity);
    }

    public Entity<?> getEntity() {
        return entity;
    }

}

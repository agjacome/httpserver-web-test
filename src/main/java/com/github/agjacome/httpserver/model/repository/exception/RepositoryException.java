package com.github.agjacome.httpserver.model.repository.exception;

public class RepositoryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RepositoryException() {
        super();
    }

    public RepositoryException(final String message) {
        super(message);
    }

    public RepositoryException(final Throwable cause) {
        super(cause);
    }

    public RepositoryException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

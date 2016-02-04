package com.github.agjacome.httpserver.server.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpRequest;

import static java.util.Objects.requireNonNull;

public final class RequestLogger implements ServerRequestEffect {

    private final Logger logger;

    public static final RequestLogger logRequest = new RequestLogger(
        LoggerFactory.getLogger(RequestLogger.class)
    );

    public RequestLogger(final Logger logger) {
        this.logger = requireNonNull(logger);
    }

    @Override
    public void effect(final ServerRequest request) {
        final HttpRequest req = request.getHttpRequest();
        logger.info(
            "Connection established with {}:{} → {} {}",
            req.getRemoteAddress().getHostString(),
            req.getRemoteAddress().getPort(),
            req.getMethod().getName(),
            req.getURI().getPath()
        );
    }

}

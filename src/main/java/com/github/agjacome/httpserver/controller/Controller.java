package com.github.agjacome.httpserver.controller;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.action.ServerRequestEffect;
import com.github.agjacome.httpserver.server.http.HttpHeader;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public abstract class Controller implements ServerRequestEffect {

    @Override
    public final void effect(final ServerRequest request) {
        synchronized (request) {
            if (request.isCompleted()) return;

            try {
                handleRequest(request);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override // Overridden and final to enforce this.effect synchronized
    public final void accept(final ServerRequest request) {
        effect(request);
    }

    @Override // Overridden and final to enforce this.effect synchronized
    public final void call(final ServerRequest request) {
        effect(request);
    }

    protected abstract void handleRequest(
        final ServerRequest request
    ) throws Exception;

    protected final HttpHeader header(
        final String key, final String ... values
    ) {
        return new HttpHeader(uncased(key), values);
    }

}

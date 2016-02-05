package com.github.agjacome.httpserver.controller;

import java.util.Map;
import java.util.Optional;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.action.ServerRequestEffect;
import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.server.http.HttpQueryParameter;
import com.github.agjacome.httpserver.server.http.HttpRequest;

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

    protected HttpHeader header(
        final String key, final String ... values
    ) {
        return new HttpHeader(uncased(key), values);
    }

    protected Optional<String> getFirstParamFor(
        final String name, final HttpRequest request
    ) {
        return request.getQueryParameter(name).flatMap(p -> p.getFirst());
    }

    protected Optional<String> getFirstParamFor(
        final String name,
        final Map<String, HttpQueryParameter> params
    ) {
        return Optional.ofNullable(params.get(name)).flatMap(p -> p.getFirst());
    }

}

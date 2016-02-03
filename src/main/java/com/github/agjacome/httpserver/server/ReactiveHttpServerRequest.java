package com.github.agjacome.httpserver.server;

import com.github.agjacome.httpserver.server.http.HttpExchangeRequestAdapter;
import com.github.agjacome.httpserver.server.http.HttpExchangeResponseBuilder;
import com.github.agjacome.httpserver.server.http.HttpRequest;
import com.github.agjacome.httpserver.server.http.HttpResponseBuilder;
import com.sun.net.httpserver.HttpExchange;

import static java.util.Objects.requireNonNull;

final class ReactiveHttpServerRequest implements ServerRequest {

    private final HttpRequest         request;
    private final HttpResponseBuilder response;

    private boolean completed = false;

    ReactiveHttpServerRequest(final HttpExchange exchange) {
        requireNonNull(exchange);

        this.request  = new HttpExchangeRequestAdapter(exchange);
        this.response = new HttpExchangeResponseBuilder(exchange);
    }

    @Override
    public synchronized HttpRequest getHttpRequest() {
        if (completed) throw new IllegalStateException("Call on already completed request.");
        return request;
    }

    @Override
    public synchronized HttpResponseBuilder getHttpResponseBuilder() {
        if (completed) throw new IllegalStateException("Call on already completed request.");
        return response;
    }

    @Override
    public synchronized void complete() {
        this.completed = true;
    }

    @Override
    public synchronized boolean isCompleted() {
        return completed;
    }

}

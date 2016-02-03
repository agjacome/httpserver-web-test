package com.github.agjacome.httpserver.server.http;

import java.util.function.Predicate;

import com.sun.net.httpserver.HttpExchange;

@FunctionalInterface
public interface HttpExchangeMatcher extends Predicate<HttpExchange> {

    public boolean matches(final HttpExchange exchange);

    @Override
    public default boolean test(final HttpExchange exchange) {
        return matches(exchange);
    }

}

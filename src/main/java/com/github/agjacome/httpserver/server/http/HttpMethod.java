package com.github.agjacome.httpserver.server.http;

import java.util.Optional;

import com.sun.net.httpserver.HttpExchange;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface HttpMethod extends HttpExchangeMatcher {

    public String getName();

    public default boolean isIdempotent() {
        return false;
    }

    public default boolean isSafe() {
        return false;
    }

    @Override
    public default boolean matches(final HttpExchange exchange) {
        return exchange.getRequestMethod().equals(getName());
    }


    // https://www.iana.org/assignments/http-methods/http-methods.xhtml
    public static enum StandardHttpMethod implements HttpMethod {

        ACL(false, true),
        BASELINE_CONTROL(false, true),
        BIND(false, true),
        CHECKIN(false, true),
        CHECKOUT(false, true),
        CONNECT(false, false),
        COPY(false, true),
        DELETE(false, true),
        GET(true, true),
        HEAD(true, true),
        LABEL(false, true),
        LINK(false, true),
        LOCK(false, false),
        MERGE(false, true),
        MKACTIVITY(false, true),
        MKCALENDAR(false, true),
        MKCOL(false, true),
        MKREDIRECTREF(false, true),
        MKWORKSPACE(false, true),
        MOVE(false, true),
        OPTIONS(true, true),
        ORDERPATCH(false, true),
        PATCH(false, false),
        POST(false, false),
        PRI(true, true),
        PROPFIND(true, true),
        PROPPATCH(false, true),
        PUT(false, true),
        REBIND(false, true),
        REPORT(true, true),
        SEARCH(true, true),
        TRACE(true, true),
        UNBIND(false, true),
        UNCHECKOUT(false, true),
        UNLINK(false, true),
        UNLOCK(false, true),
        UPDATE(false, true),
        UPDATEREDIRECTREF(false, true),
        VERSION_CONTROL(false, true);

        private final boolean safe;
        private final boolean idempotent;

        public static Optional<HttpMethod> of(final String name) {
            final String method = requireNonNull(name).replace('-', '_');

            try {
                return Optional.of(StandardHttpMethod.valueOf(method));
            } catch (final IllegalArgumentException iae) {
                return Optional.empty();
            }
        }

        private StandardHttpMethod(final boolean safe, final boolean idempotent) {
            this.safe       = safe;
            this.idempotent = idempotent;
        }

        @Override
        public String getName() {
            return name().replace('_', '-');
        }

        @Override public boolean isIdempotent() { return idempotent; }
        @Override public boolean isSafe()       { return safe;       }

    }

}


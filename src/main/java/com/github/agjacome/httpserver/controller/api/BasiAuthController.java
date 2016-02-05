package com.github.agjacome.httpserver.controller.api;

import java.util.Base64;
import java.util.Optional;

import com.github.agjacome.httpserver.controller.Controller;
import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.server.http.HttpRequest;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public abstract class BasiAuthController extends Controller {

    protected boolean hasAuthHeader(final HttpRequest request) {
        return getAuthHeader(request).isPresent();
    }

    protected Optional<HttpHeader> getAuthHeader(final HttpRequest request) {
        return request.getHeader(uncased("Authorization"));
    }

    protected Optional<CaseInsensitiveString> getAuthUsername(
        final HttpRequest request
    ) {
        return getAuthHeader(request).flatMap(this::getAuthUsername);
    }

    protected Optional<CaseInsensitiveString> getAuthUsername(
        final HttpHeader header
    ) {
        return header.getFirst()
              .flatMap(value -> parseTokenValue(value, 0))
              .map(CaseInsensitiveString::uncased);
    }

    protected Optional<String> getAuthPassword(final HttpRequest request) {
        return getAuthHeader(request).flatMap(this::getAuthPassword);
    }

    protected Optional<String> getAuthPassword(
        final HttpHeader header
    ) {
        return header.getFirst().flatMap(value -> parseTokenValue(value, 1));
    }

    private Optional<String> parseTokenValue(final String value, final int index) {
        try {

            final String encoded = value.trim().substring("Basic ".length());
            final String decoded = new String(Base64.getDecoder().decode(encoded.trim()));

            final String[] tokens = decoded.split(":");
            return index >= 0 && index < tokens.length
                 ? Optional.of(tokens[index])
                 : Optional.empty();

        } catch (final IllegalArgumentException iae) {
            return Optional.empty();
        }
    }

}

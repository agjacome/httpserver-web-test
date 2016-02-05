package com.github.agjacome.httpserver.controller;

import java.util.Map;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpRequest;
import com.github.agjacome.httpserver.server.http.HttpResponse;
import com.github.agjacome.httpserver.view.View;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.server.http.HttpStatusCode.StandardHttpStatusCode.OK;

public class LoginGetController extends Controller {

    private final View view;

    public static Controller loginGetController(final View view) {
        return new LoginGetController(view);
    }

    public LoginGetController(final View view) {
        this.view = requireNonNull(view);
    }

    @Override
    protected void handleRequest(final ServerRequest request) throws Exception {
        final Map<String, String> values = getViewValues(
            request.getHttpRequest()
        );

        final HttpResponse response = request.getHttpResponseBuilder()
            .withStatusCode(OK)
            .withHeader(view.getContentType())
            .withContentLength(view.getContentLength(values))
            .build();

        view.render(values, response.getBodyOutputStream());
        request.complete();
    }

    private Map<String, String> getViewValues(final HttpRequest request) {
        return request.getQueryParameter("redirect")
              .filter(p -> !p.getValues().isEmpty())
              .map(p -> singletonMap("redirect", p.getValues().get(0)))
              .orElse(emptyMap());
    }

}

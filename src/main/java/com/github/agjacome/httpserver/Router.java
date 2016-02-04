package com.github.agjacome.httpserver;

import java.util.Arrays;
import java.util.List;

import com.github.agjacome.httpserver.server.Server;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.controller.NotFoundController.notFoundController;
import static com.github.agjacome.httpserver.server.action.RequestLogger.logRequest;
import static com.github.agjacome.httpserver.server.filter.CompletedFilter.uncompleted;
import static com.github.agjacome.httpserver.view.NotFoundView.notFoundView;

public final class Router {

    private final List<Route> routes;

    public static Router of(final Route ... routes) {
        return new Router(Arrays.asList(routes));
    }

    public Router(final List<Route> routes) {
        this.routes = requireNonNull(routes);
    }

    public void enroute(final Server server) {
        server.requests()
              .forEach(logRequest);

        routes.forEach(route -> route.enroute(server.requests()));

        server.requests()
              .filter(uncompleted)
              .subscribe(notFoundController(notFoundView));
    }

}

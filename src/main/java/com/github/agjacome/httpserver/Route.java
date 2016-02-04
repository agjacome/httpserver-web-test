package com.github.agjacome.httpserver;

import com.github.agjacome.httpserver.controller.Controller;
import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.filter.ServerRequestFilter;

import static com.github.agjacome.httpserver.server.filter.CompletedFilter.uncompleted;
import static com.github.agjacome.httpserver.util.contract.NullContracts.requireAllNonNull;

import rx.Observable;

public interface Route {

    public static Route route(
        final ServerRequestFilter filter, final Controller controller
    ) {
        requireAllNonNull(controller, filter);
        return new Route() {
            @Override public Controller controller() { return controller; }
            @Override public ServerRequestFilter filter() { return filter; }
        };
    }

    public Controller controller();

    public ServerRequestFilter filter();

    public default void enroute(final Observable<ServerRequest> requests) {
        requests.filter(uncompleted.and(filter()))
                .subscribe(controller());
    }

}

package com.github.agjacome.httpserver.server;

import java.util.function.Consumer;

import rx.Observable;

public interface Server {

    public ServerCancelable run();

    public Server afterRun(final Consumer<Server> consumer);

    public Observable<ServerRequest> requests();

}

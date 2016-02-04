package com.github.agjacome.httpserver.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.function.Consumer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import static java.util.Objects.requireNonNull;

import static org.slf4j.LoggerFactory.getLogger;

import rx.Observable;
import rx.Subscriber;

public final class ReactiveHttpServer implements Server, ServerCancelable {

    private final HttpServer server;

    private final List<ServerObserver> observers = new LinkedList<>();

    private final List<Consumer<Server>> afterRun       = new LinkedList<>();
    private final List<Consumer<Server>> beforeShutdown = new LinkedList<>();

    public ReactiveHttpServer(
        final ServerConfiguration configuration
    ) throws IOException {
        requireNonNull(configuration);

        server = HttpServer.create();
        server.bind(configuration.getAddress(), configuration.getBacklog());
        server.setExecutor(configuration.getExecutor());
        server.createContext(configuration.getPath(), mainHandler());

        setUpLogging(configuration.getAddress(), configuration.getPath());
    }

    @Override
    public Server afterRun(final Consumer<Server> consumer) {
        afterRun.add(consumer);
        return this;
    }

    @Override
    public ServerCancelable run() {
        server.start();
        afterRun.forEach(c -> c.accept(this));
        return this;
    }

    @Override
    public Observable<ServerRequest> requests() {
        return Observable.create(subscriber -> {
            completeOnShutdown(subscriber);
            observers.add(request -> handleOnNext(request, subscriber));
        });
    }

    @Override
    public ServerCancelable beforeShutdown(final Consumer<Server> consumer) {
        beforeShutdown.add(consumer);
        return this;
    }

    @Override
    public RunnableFuture<Void> shutdown() {
        beforeShutdown.forEach(c -> c.accept(this));

        return new FutureTask<>(() -> {
            server.stop(0);
            return null;
        });
    }


    private void setUpLogging(
        final InetSocketAddress serverAddress, final String serverPath
    ) {
        afterRun(srv -> getLogger(getClass()).info(
            "Service bound to {}:{} on {}",
            serverAddress.getHostString(),
            serverAddress.getPort(),
            serverPath
        ));

        beforeShutdown(srv -> getLogger(getClass()).info(
            "Stopping service bound to {}:{} on {}",
            serverAddress.getHostString(),
            serverAddress.getPort(),
            serverPath
        ));
    }

    private HttpHandler mainHandler() {
        return exchange -> {
            final ServerRequest req = new ReactiveHttpServerRequest(exchange);
            observers.forEach(observer -> observer.handleRequest(req));
        };
    }

    private void completeOnShutdown(
        final Subscriber<? super ServerRequest> subscriber
    ) {
        beforeShutdown(s -> {
            if (!subscriber.isUnsubscribed()) subscriber.onCompleted();
        });
    }

    private void handleOnNext(
        final ServerRequest request,
        final Subscriber<? super ServerRequest> subscriber
    ) {
        try {
            if (!subscriber.isUnsubscribed()) subscriber.onNext(request);
        } catch (final Throwable t) {
            getLogger(getClass()).error("Error on request handling", t);
            if (!subscriber.isUnsubscribed()) subscriber.onError(t);
        }
    }

}

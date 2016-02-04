package com.github.agjacome.httpserver.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.function.Consumer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import static java.util.Objects.requireNonNull;

import rx.Observable;
import rx.Subscriber;

public final class ReactiveHttpServer implements Server, ServerCancelable {

    private final HttpServer server;

    private final List<ServerObserver> observers = new LinkedList<>();

    private final List<Consumer<Server>> onStartConsumers = new LinkedList<>();
    private final List<Consumer<Server>> onStopConsumers  = new LinkedList<>();

    public ReactiveHttpServer(
        final ServerConfiguration configuration
    ) throws IOException {
        requireNonNull(configuration);

        server = HttpServer.create();
        server.bind(configuration.getAddress(), configuration.getBacklog());
        server.setExecutor(configuration.getExecutor());
        server.createContext(configuration.getPath(), mainHandler());
    }

    @Override
    public Server onRun(final Consumer<Server> consumer) {
        onStartConsumers.add(consumer);
        return this;
    }

    @Override
    public ServerCancelable run() {
        onStartConsumers.forEach(c -> c.accept(this));
        server.start();
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
    public ServerCancelable onShutdown(final Consumer<Server> consumer) {
        onStopConsumers.add(consumer);
        return this;
    }

    @Override
    public RunnableFuture<Void> shutdown() {
        onStopConsumers.forEach(c -> c.accept(this));

        return new FutureTask<>(() -> {
            server.stop(0);
            return null;
        });
    }


    private HttpHandler mainHandler() {
        return exchange -> {
            final ServerRequest r = new ReactiveHttpServerRequest(exchange);
            observers.forEach(o -> o.handleRequest(r));
        };
    }

    private void completeOnShutdown(
        final Subscriber<? super ServerRequest> subscriber
    ) {
        onShutdown(s -> {
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
            if (!subscriber.isUnsubscribed()) subscriber.onError(t);
        }
    }

}

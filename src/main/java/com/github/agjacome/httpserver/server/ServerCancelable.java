package com.github.agjacome.httpserver.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RunnableFuture;
import java.util.function.Consumer;

public interface ServerCancelable {

    public ServerCancelable onShutdown(final Consumer<Server> consumer);

    public RunnableFuture<Void> shutdown();

    public default void shutdownNow() {
        shutdown().run();
    }

    public default void awaitShutdown() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        onShutdown(s -> latch.countDown());
        latch.await();
    }

}

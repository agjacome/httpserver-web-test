package com.github.agjacome.httpserver.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.util.contract.StringContracts.requirePrefix;

public final class ReactiveHttpServerConfiguration implements ServerConfiguration {

    private final InetSocketAddress address;
    private final int               backlog;
    private final Executor          executor;
    private final String            path;

    public ReactiveHttpServerConfiguration(
        final InetSocketAddress address,
        final int               backlog,
        final Executor          executor,
        final String            path
    ) {
        this.address  = requireNonNull(address);
        this.backlog  = backlog;
        this.executor = requireNonNull(executor);
        this.path     = requirePrefix(requireNonNull(path), "/");
    }

    @Override
    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public int getBacklog() {
        return backlog;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

}

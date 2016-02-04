package com.github.agjacome.httpserver.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import static com.github.agjacome.httpserver.util.contract.NullContracts.requireAllNonNull;
import static com.github.agjacome.httpserver.util.contract.StringContracts.requireNotSuffix;
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
        requireAllNonNull(address, executor, path);
        requireNotSuffix(requirePrefix(path, "/"), "/");

        this.address  = address;
        this.backlog  = backlog;
        this.executor = executor;
        this.path     = path;
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

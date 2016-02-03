package com.github.agjacome.httpserver.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public interface ServerConfiguration {

    public InetSocketAddress getAddress();

    public int getBacklog();

    public String getPath();

    public Executor getExecutor();

}

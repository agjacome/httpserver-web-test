package com.github.agjacome.httpserver.server;

import com.github.agjacome.httpserver.server.http.HttpRequest;
import com.github.agjacome.httpserver.server.http.HttpResponseBuilder;

public interface ServerRequest {

    public HttpRequest getHttpRequest();

    public HttpResponseBuilder getHttpResponseBuilder();

    public void complete();

    public boolean isCompleted();

}

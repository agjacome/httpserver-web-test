package com.github.agjacome.httpserver.server.http;

import java.io.IOException;

public interface HttpResponseBuilder {

    public HttpResponseBuilder withStatusCode(final HttpStatusCode code);

    public HttpResponseBuilder withContentLength(final long length);

    public HttpResponseBuilder withHeader(final HttpHeader header);

    public HttpResponse build() throws IOException;

}

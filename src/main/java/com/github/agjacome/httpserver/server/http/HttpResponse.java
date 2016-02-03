package com.github.agjacome.httpserver.server.http;

import java.io.OutputStream;
import java.util.Optional;

import com.github.agjacome.httpserver.util.CaseInsensitiveString;

public interface HttpResponse {

    public HttpStatusCode getStatusCode();

    public long getContentLength();

    public Optional<HttpHeader> getHeader(final CaseInsensitiveString key);

    public OutputStream getBodyOutputStream();

}

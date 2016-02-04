package com.github.agjacome.httpserver.server.http;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Optional;

import com.github.agjacome.httpserver.util.CaseInsensitiveString;

public interface HttpRequest {

    public InetSocketAddress getRemoteAddress();

    public HttpMethod getMethod();

    public URI getURI();

    public String getPath();

    public Optional<HttpQueryParameter> getQueryParameter(final String key);

    public HttpVersion getVersion();

    public Optional<HttpHeader> getHeader(final CaseInsensitiveString key);

    public InputStream getBodyInputStream();

}

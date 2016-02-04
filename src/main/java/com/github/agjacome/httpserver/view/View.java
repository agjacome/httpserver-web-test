package com.github.agjacome.httpserver.view;

import java.io.OutputStream;

import com.github.agjacome.httpserver.server.http.HttpHeader;

public interface View {

    public HttpHeader getContentType();

    public long getContentLength() throws Exception;

    public void writeView(final OutputStream stream) throws Exception;

}

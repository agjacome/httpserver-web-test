package com.github.agjacome.httpserver.view;

import java.io.OutputStream;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.server.http.HttpHeader;

public interface View {

    public boolean isAccessibleBy(final User user);

    public HttpHeader getContentType();

    public long getContentLength() throws Exception;

    public void writeView(final OutputStream stream) throws Exception;

}

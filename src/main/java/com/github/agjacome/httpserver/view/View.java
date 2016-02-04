package com.github.agjacome.httpserver.view;

import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.server.http.HttpHeader;

public interface View {

    public boolean isAccessibleBy(final User user);

    public HttpHeader getContentType();

    public long getContentLength(
        final Map<String, String> values
    ) throws Exception;

    public void render(
         final Map<String, String> values, final OutputStream stream
    ) throws Exception;


    public default long getContentLength() throws Exception {
        return getContentLength(Collections.emptyMap());
    }

    public default void render(final OutputStream stream) throws Exception {
        render(Collections.emptyMap(), stream);
    }


}

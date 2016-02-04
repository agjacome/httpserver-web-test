package com.github.agjacome.httpserver.view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.util.Resources;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public abstract class HttpView implements View {

    private final String content;

    protected HttpView(final String content) {
        this.content = requireNonNull(content);
    }

    @Override
    public HttpHeader getContentType() {
        return new HttpHeader(uncased("Content-Type"), "text/html");
    }

    @Override
    public long getContentLength() throws Exception {
        return content.getBytes().length;
    }

    @Override
    public void writeView(final OutputStream stream) throws Exception {
        try (final Writer writer = new OutputStreamWriter(stream)) {
            writer.append(content);
            writer.flush();
        }
    }

    protected String getHeader() throws IOException {
        return Resources.readResourceAsString("/view/header.html");
    }

    protected String getFooter() throws IOException {
        return Resources.readResourceAsString("/view/footer.html");
    }

}

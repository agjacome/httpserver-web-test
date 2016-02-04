package com.github.agjacome.httpserver.view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Set;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.util.Resources;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public abstract class HtmlView implements View {

    protected abstract String    getContent() throws IOException;
    protected abstract Set<Role> getRolesAllowed();

    @Override
    public boolean isAccessibleBy(final User user) {
        return getRolesAllowed().stream().anyMatch(user::hasRole);
    }

    @Override
    public HttpHeader getContentType() {
        return new HttpHeader(uncased("Content-Type"), "text/html");
    }

    @Override
    public long getContentLength() throws IOException {
        return getContent().getBytes().length;
    }

    @Override
    public void writeView(final OutputStream stream) throws Exception {
        try (final Writer writer = new OutputStreamWriter(stream)) {
            writer.append(getContent());
            writer.flush();
        }
    }

    protected String getHeader() throws IOException {
        return Resources.readResourceAsString("view/header.html");
    }

    protected String getFooter() throws IOException {
        return Resources.readResourceAsString("view/footer.html");
    }

}

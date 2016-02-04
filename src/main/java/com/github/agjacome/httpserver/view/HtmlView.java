package com.github.agjacome.httpserver.view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.server.http.HttpHeader;
import com.github.agjacome.httpserver.util.Resources;

import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public abstract class HtmlView implements View {

    protected abstract Set<Role> getRolesAllowed();

    protected abstract String getContent(
        final Map<String, String> values
    ) throws IOException;

    @Override
    public boolean isAccessibleBy(final User user) {
        return getRolesAllowed().stream().anyMatch(user::hasRole);
    }

    @Override
    public HttpHeader getContentType() {
        return new HttpHeader(uncased("Content-Type"), "text/html");
    }

    @Override
    public long getContentLength(
        final Map<String, String> values
    ) throws IOException {
        return getContent(values).getBytes().length;
    }

    @Override
    public void render(
        final Map<String, String> values,
        final OutputStream stream
    ) throws Exception {
        try (final Writer writer = new OutputStreamWriter(stream)) {
            writer.append(getContent(values));
            writer.flush();
        }
    }

    protected String getHeader() throws IOException {
        return Resources.readResourceAsString("view/header.html");
    }

    protected String getFooter() throws IOException {
        return Resources.readResourceAsString("view/footer.html");
    }

    protected String replaceTemplate(
        final String page, final String key, final String value
    ) {
        return page.replace(String.format("${%s}", key.toUpperCase()), value);
    }

}

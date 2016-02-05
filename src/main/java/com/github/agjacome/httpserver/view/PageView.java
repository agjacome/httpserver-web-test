package com.github.agjacome.httpserver.view;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.util.Arrays;
import com.github.agjacome.httpserver.util.Resources;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.model.User.Role.ADMIN;

public final class PageView extends HtmlView {

    private final String    name;
    private final Set<Role> roles;

    public static View pageView(final String name, final Role ... roles) {
        return new PageView(name, Arrays.asSet(roles));
    }

    public PageView(final String name, final Set<Role> roles) {
        this.name  = requireNonNull(name);
        this.roles = requireNonNull(roles);
        this.roles.add(ADMIN);
    }

    @Override
    protected Set<Role> getRolesAllowed() {
        return unmodifiableSet(roles);
    }

    @Override
    protected String getContent(final Map<String, String> values) throws IOException {
        final String head = getHeader();
        final String foot = getFooter();

        final String body = formatBody(
            values, Resources.readResourceAsString("view/page.html")
        );

        return new StringJoiner("\n").add(head).add(body).add(foot).toString();
    }

    private String formatBody(
        final Map<String, String> values, final String body
    ) {
        final String user = values.getOrDefault("username", "");

        final String fb1 = replaceTemplate(body, "username", user);
        final String fb2 = replaceTemplate(fb1 , "pagename", name);

        return fb2;
    }

}

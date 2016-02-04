package com.github.agjacome.httpserver.view;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.util.Resources;

public class NotFoundView extends HtmlView {

    public static final View notFoundView = new NotFoundView();

    @Override
    protected Set<Role> getRolesAllowed() {
        return EnumSet.allOf(Role.class);
    }

    @Override
    protected String getContent(
        final Map<String, String> values
    ) throws IOException {
        final String head = getHeader();
        final String body = Resources.readResourceAsString("view/404.html");
        final String foot = getFooter();

        return new StringJoiner("\n").add(head).add(body).add(foot).toString();
    }

}

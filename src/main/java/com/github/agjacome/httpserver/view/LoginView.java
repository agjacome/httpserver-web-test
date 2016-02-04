package com.github.agjacome.httpserver.view;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import java.util.StringJoiner;

import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.util.Resources;

public class LoginView extends HtmlView {

    public static final View loginView = new LoginView();

    @Override
    protected String getContent() throws IOException {
        final String head = getHeader();
        final String body = Resources.readResourceAsString("view/login.html");
        final String foot = getFooter();

        return new StringJoiner("\n").add(head).add(body).add(foot).toString();
    }

    @Override
    protected Set<Role> getRolesAllowed() {
        return EnumSet.allOf(Role.class);
    }

}

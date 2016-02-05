package com.github.agjacome.httpserver.view;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.util.Resources;

public final class LoginView extends HtmlView {

    public static final View loginView = new LoginView();

    @Override
    protected String getContent(
        final Map<String, String> values
    ) throws IOException {
        final String body = Resources.readResourceAsString("view/login.html");
        final String formatted = formatBody(values, body);

        return new StringJoiner("\n").add(getHeader()).add(formatted).add(getFooter()).toString();
    }

    @Override
    protected Set<Role> getRolesAllowed() {
        return EnumSet.allOf(Role.class);
    }

    private String formatBody(
        final Map<String, String> values, final String body
    ) {
        if (values.containsKey("redirect")) {
            final String value = values.get("redirect");
            return replaceTemplate(body, "redirect", "<input type=\"hidden\" name=\"redirect\" value=\"" + value + "\">");
        }

        return replaceTemplate(body, "redirect", "");
    }
}

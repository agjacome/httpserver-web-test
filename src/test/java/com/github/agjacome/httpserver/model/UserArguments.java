package com.github.agjacome.httpserver.model;

import java.util.Set;

import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.util.Arrays;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;
import com.github.agjacome.httpserver.util.PasswordVerifier;
import com.github.agjacome.httpserver.util.annotation.DisallowConstruction;

import static com.github.agjacome.httpserver.model.User.Role.ADMIN;
import static com.github.agjacome.httpserver.model.User.Role.PAGE_1;
import static com.github.agjacome.httpserver.model.User.Role.PAGE_2;
import static com.github.agjacome.httpserver.util.CaseInsensitiveString.uncased;

public final class UserArguments {

    @DisallowConstruction
    private UserArguments() { }

    public static User anUser() {
        return new User(anUsername(), aPasswordVerifier(), aRoleSet());
    }

    public static CaseInsensitiveString anUsername() {
        return uncased("username");
    }

    public static PasswordVerifier aPasswordVerifier() {
        return "password"::equals;
    }

    public static String validPassword() {
        return "password";
    }

    public static String invalidPassword() {
        return "incorrectPassword";
    }

    public static Set<Role> aRoleSet() {
        return Arrays.asSet(ADMIN, PAGE_2);
    }

    public static Role anIncludedRole() {
        return ADMIN;
    }

    public static Role aNotIncludedRole() {
        return PAGE_1;
    }

}

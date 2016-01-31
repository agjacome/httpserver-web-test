package com.github.agjacome.httpserver.model.repository;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;

public interface UserRepository extends Repository<CaseInsensitiveString, User> {

    public default ResultBuilder<User> searchByUsername(final String pattern) {
        return search(u -> u.getUsername().contains(pattern));
    }

    public default ResultBuilder<User> searchByRole(final Role role) {
        return search(u -> u.hasRole(role));
    }

}

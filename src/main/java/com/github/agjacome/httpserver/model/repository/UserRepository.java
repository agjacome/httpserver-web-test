package com.github.agjacome.httpserver.model.repository;

import java.util.List;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.model.User.Role;
import com.github.agjacome.httpserver.model.repository.exception.RepositoryException;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;

public interface UserRepository extends Repository<CaseInsensitiveString, User> {

    public default ResultBuilder<User, List<User>> searchByUsername(
        final String pattern
    ) throws RepositoryException {
        return search(u -> u.getUsername().contains(pattern));
    }

    public default ResultBuilder<User, List<User>> searchByRole(
        final Role role
    ) throws RepositoryException {
        return search(u -> u.hasRole(role));
    }

}

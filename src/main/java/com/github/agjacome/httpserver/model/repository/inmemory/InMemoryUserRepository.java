package com.github.agjacome.httpserver.model.repository.inmemory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.agjacome.httpserver.model.User;
import com.github.agjacome.httpserver.model.repository.UserRepository;
import com.github.agjacome.httpserver.util.CaseInsensitiveString;

public class InMemoryUserRepository extends InMemoryRepository<CaseInsensitiveString, User> implements UserRepository {

    public static UserRepository empty() {
        return of(Collections.emptyList());
    }

    public static UserRepository of(final User ... users) {
        return of(Arrays.asList(users));
    }

    public static UserRepository of(final Collection<User> users) {
        return new InMemoryUserRepository(new LinkedHashSet<>(users));
    }

    private InMemoryUserRepository(final Set<User> users) {
        users.forEach(this::create);
    }

}
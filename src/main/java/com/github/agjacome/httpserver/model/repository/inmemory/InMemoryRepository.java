package com.github.agjacome.httpserver.model.repository.inmemory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.agjacome.httpserver.model.Entity;
import com.github.agjacome.httpserver.model.repository.Repository;
import com.github.agjacome.httpserver.model.repository.ResultBuilder;
import com.github.agjacome.httpserver.model.repository.exception.EntityAlreadyExistsException;
import com.github.agjacome.httpserver.model.repository.exception.EntityDoesNotExistException;
import com.github.agjacome.httpserver.model.repository.exception.RepositoryException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

abstract class InMemoryRepository<I, E extends Entity<I>> implements Repository<I, E> {

    protected final ConcurrentMap<I, E> repository = new ConcurrentHashMap<>();

    @Override
    public Optional<E> get(final I id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public ResultBuilder<E, List<E>> search(
        final Predicate<E> query
    ) {
        return StreamResultBuilder.of(
            repository.values().stream().filter(query), Collectors.toList()
        );
    }

    @Override
    public int count(final Predicate<E> query) {
        return (int) repository.values().stream().filter(query).count();
    }

    @Override
    public E create(final E entity) {
        if (nonNull(repository.putIfAbsent(entity.getId(), entity)))
            throw new EntityAlreadyExistsException(entity);

        return entity;
    }

    @Override
    public E update(final E entity) {
        if (isNull(repository.replace(entity.getId(), entity)))
            throw new EntityDoesNotExistException(entity);

        return entity;
    }

    @Override // Overriden to preserve ConcurrentMap atomicity
    public E save(final E entity) throws RepositoryException {
        repository.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(final E entity) {
        if (isNull(repository.remove(entity.getId())))
            throw new EntityDoesNotExistException(entity);
    }

}

package com.github.agjacome.httpserver.model.repository;

import java.util.Optional;
import java.util.function.Predicate;

import com.github.agjacome.httpserver.model.Entity;
import com.github.agjacome.httpserver.model.repository.exception.RepositoryException;

public interface Repository<I, E extends Entity<I>> {

    public Optional<E> get(final I id) throws RepositoryException;

    public ResultBuilder<E> search(final Predicate<E> query) throws RepositoryException;

    public int count(final Predicate<E> query) throws RepositoryException;

    public E create(final E entity) throws RepositoryException;

    public E update(final E entity) throws RepositoryException;

    public void delete(final E entity) throws RepositoryException;


    public default E save(final E entity) throws RepositoryException {
        return get(entity.getId())
              .map(this::update)
              .orElseGet(() -> create(entity));
    }

    public default ResultBuilder<E> list() throws RepositoryException {
        return search(e -> true);
    }

    public default int count() throws RepositoryException {
        return count(e -> true);
    }

}

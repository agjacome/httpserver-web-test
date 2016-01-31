package com.github.agjacome.httpserver.model.repository;

import java.util.Collection;
import java.util.Comparator;

public interface ResultBuilder<E, C extends Collection<E>> {

    public ResultBuilder<E, C> withPageSize(final int pageSize);

    public ResultBuilder<E, C> withPageNumber(final int pageNumber);

    public ResultBuilder<E, C> orderedBy(final Comparator<E> comparator);

    public C build();

}

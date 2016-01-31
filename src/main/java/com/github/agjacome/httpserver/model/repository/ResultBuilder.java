package com.github.agjacome.httpserver.model.repository;

import java.util.Collection;
import java.util.Comparator;

public interface ResultBuilder<A> {

    public ResultBuilder<A> withPageSize(final int pageSize);

    public ResultBuilder<A> withPageNumber(final int pageNumber);

    public ResultBuilder<A> orderedBy(final Comparator<A> comparator);

    public Collection<A> build();

}

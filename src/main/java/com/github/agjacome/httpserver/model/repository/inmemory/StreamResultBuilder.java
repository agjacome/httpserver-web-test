package com.github.agjacome.httpserver.model.repository.inmemory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.agjacome.httpserver.model.repository.ResultBuilder;

import static java.util.Objects.requireNonNull;

import static com.github.agjacome.httpserver.util.contract.NumericContracts.requireNonNegative;
import static com.github.agjacome.httpserver.util.contract.NumericContracts.requirePositive;

public final class StreamResultBuilder<E, C extends Collection<E>> implements ResultBuilder<E, C> {

    private final Stream<E>          stream;
    private final Collector<E, ?, C> collector;

    private int           pageNumber = 1;
    private int           pageSize   = Integer.MAX_VALUE;
    private Comparator<E> comparator = (a1, a2) -> 1;

    public static <E> ResultBuilder<E, List<E>> empty() {
        return new StreamResultBuilder<>(Stream.empty(), Collectors.toList());
    }

    public static <E, C extends Collection<E>> ResultBuilder<E, C> of(
        final Stream<E> stream, final Collector<E, ?, C> collector
    ) {
        return new StreamResultBuilder<>(stream, collector);
    }

    private StreamResultBuilder(
        final Stream<E> stream, final Collector<E, ?, C> collector
    ) {
        this.stream    = requireNonNull(stream);
        this.collector = requireNonNull(collector);
    }

    @Override
    public ResultBuilder<E, C> withPageSize(final int pageSize) {
        this.pageSize = requireNonNegative(pageSize);
        return this;
    }

    @Override
    public ResultBuilder<E, C> withPageNumber(final int pageNumber) {
        this.pageNumber = requirePositive(pageNumber);
        return this;
    }

    @Override
    public ResultBuilder<E, C> orderedBy(final Comparator<E> comparator) {
        this.comparator = requireNonNull(comparator);
        return this;
    }

    @Override
    public C build() {
        final int pageOffset = pageSize * (pageNumber - 1);

        return stream
              .sorted(comparator)
              .skip(pageOffset).limit(pageSize)
              .collect(collector);
    }

}

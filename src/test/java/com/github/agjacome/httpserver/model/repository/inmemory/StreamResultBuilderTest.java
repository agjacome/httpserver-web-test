package com.github.agjacome.httpserver.model.repository.inmemory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.github.agjacome.httpserver.model.repository.ResultBuilder;
import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(Theories.class)
public class StreamResultBuilderTest {

    private <E> ResultBuilder<E, List<E>> builder(final Collection<E> es) {
        return StreamResultBuilder.of(es.stream(), Collectors.toList());
    }

    @Test
    public void of_constructor_must_throw_exception_on_null_stream() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> StreamResultBuilder.of(null, Collectors.toList()));
    }

    @Test
    public void of_constructor_must_throw_exception_on_null_collector() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> StreamResultBuilder.of(Stream.empty(), null));
    }

    @Test
    public void empty_constructor_must_create_an_empty_collection_on_build() {
        assertThat(StreamResultBuilder.empty().build()).isEmpty();
    }

    @Theory(nullsAccepted = false)
    public void build_must_return_exactly_same_elements_if_not_configured(
        @ForAll final Collection<Object> elements
    ) {
        final List<Object> result = builder(elements).build();
        assertThat(result).containsExactlyElementsOf(elements);
    }

    @Theory(nullsAccepted = false)
    public void with_page_size_must_throw_exception_on_negative(
        @ForAll final Collection<Object> elements,
        @ForAll @InRange(max = "-1") final int pageSize
    ) {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> builder(elements).withPageSize(pageSize));
    }

    @Theory(nullsAccepted = false)
    public void with_page_size_must_alter_size_of_result(
        @ForAll @Size(min = 20, max = 50) final Collection<Object> elements,
        @ForAll @InRange(min = "0", max = "20") final int pageSize
    ) {
        final List<Object> result = builder(elements).withPageSize(pageSize).build();
        assertThat(result).hasSize(pageSize);
    }

    @Theory(nullsAccepted = false)
    public void with_page_numer_must_throw_exception_on_non_positive(
        @ForAll final Collection<Object> elements,
        @ForAll @InRange(max = "0") final int pageNumber
    ) {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> builder(elements).withPageNumber(pageNumber));
    }

    @Theory(nullsAccepted = false)
    public void with_page_number_must_alter_page_of_result(
        @ForAll @Size(min = 20, max = 50) final List<Object> elements,
        @ForAll @InRange(min = "1", max = "5") final int pageNumber
    ) {
        final int pageSize = 4;

        final List<Object> result = builder(elements)
            .withPageSize(pageSize).withPageNumber(pageNumber).build();

        final int fromIndex = pageSize * (pageNumber - 1);
        assertThat(result).containsExactlyElementsOf(
            elements.subList(fromIndex, fromIndex + pageSize)
        );
    }

    @Theory(nullsAccepted = false)
    public void ordered_by_must_throw_exception_on_null(
        @ForAll final Collection<Object> elements
    ) {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> builder(elements).orderedBy(null));
    }

    @Theory(nullsAccepted = false)
    public void order_by_must_alter_order_of_result(
        @ForAll final List<Object> elements
    ) {
        final Comparator<Object> comparator = (a1, a2) -> -1;
        assertThat(builder(elements).orderedBy(comparator).build())
            .isSortedAccordingTo(comparator);
    }

}

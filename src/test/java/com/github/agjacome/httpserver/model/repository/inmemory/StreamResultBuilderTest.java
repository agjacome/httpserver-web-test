package com.github.agjacome.httpserver.model.repository.inmemory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.agjacome.httpserver.model.repository.ResultBuilder;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assume.assumeNotNull;

@RunWith(JUnitQuickcheck.class)
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

    @Property
    public <E> void build_must_return_exactly_same_elements_if_not_configured(
        final Collection<E> elements
    ) {
        assumeNotNull(elements);
        assumeNotNull(elements.toArray());

        final List<E> result = builder(elements).build();
        assertThat(result).containsExactlyElementsOf(elements);
    }

    @Property
    public <E> void with_page_size_must_throw_exception_on_negative(
        final Collection<E> elements, @InRange(max = "-1") final int pageSize
    ) {
        assumeNotNull(elements);
        assumeNotNull(elements.toArray());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> builder(elements).withPageSize(pageSize));
    }

    @Property
    public <E> void with_page_size_must_alter_size_of_result(
        @Size(min = 20, max = 50) final Collection<E> elements,
        @InRange(min = "0", max = "20") final int pageSize
    ) {
        final List<E> result = builder(elements).withPageSize(pageSize).build();
        assertThat(result).hasSize(pageSize);
    }

    @Property
    public <E> void with_page_numer_must_throw_exception_on_non_positive(
        final Collection<E> elements, @InRange(max = "0") final int pageNumber
    ) {
        assumeNotNull(elements);
        assumeNotNull(elements.toArray());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> builder(elements).withPageNumber(pageNumber));
    }

    @Property
    public <E> void with_page_number_must_alter_page_of_result(
        @Size(min = 20, max = 50) final List<E> elements,
        @InRange(min = "1", max = "5") final int pageNumber
    ) {
        assumeNotNull(elements);
        assumeNotNull(elements.toArray());

        final int pageSize  = 4;

        final List<E> result = builder(elements)
            .withPageSize(pageSize).withPageNumber(pageNumber).build();

        final int fromIndex = pageSize * (pageNumber - 1);
        final List<E> page  = elements.subList(fromIndex, fromIndex + pageSize);

        assertThat(result).containsExactlyElementsOf(page);
    }

    @Property
    public <E> void ordered_by_must_throw_exception_on_null(
        final Collection<E> elements
    ) {
        assumeNotNull(elements);
        assumeNotNull(elements.toArray());

        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> builder(elements).orderedBy(null));
    }

    @Property
    public <A> void order_by_must_alter_order_of_result(
        final List<A> elements
    ) {
        assumeNotNull(elements);
        assumeNotNull(elements.toArray());

        final Comparator<A> comparator = (a1, a2) -> -1;
        assertThat(builder(elements).orderedBy(comparator).build())
            .isSortedAccordingTo(comparator);
    }

}

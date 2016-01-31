package com.github.agjacome.httpserver.util;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitQuickcheck.class)
public class ArraysTest {

    @Property
    public <A> void as_set_must_return_set_with_only_given_values(
        final A[ ] arguments
    ) {
        assertThat(Arrays.asSet(arguments)).containsOnly(arguments);
    }

}

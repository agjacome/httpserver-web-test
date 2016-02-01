package com.github.agjacome.httpserver.util;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Theories.class)
public class ArraysTest {

    @Theory
    public void as_set_must_return_set_with_only_given_values(
        @ForAll final Object[ ] arguments
    ) {
        assertThat(Arrays.asSet(arguments)).containsOnly(arguments);
    }

}

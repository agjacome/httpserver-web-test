package com.github.agjacome.webtest;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitQuickcheck.class)
public class StringTest {

    @Property
    public void concatenationLength(final String s1, final String s2) {
        assertThat(s1.length() + s2.length()).isEqualTo((s1 + s2).length());
    }

}

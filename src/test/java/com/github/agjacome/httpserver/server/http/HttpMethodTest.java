package com.github.agjacome.httpserver.server.http;

import java.util.Set;

import org.junit.contrib.theories.DataPoints;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.github.agjacome.httpserver.server.http.HttpMethod.StandardHttpMethod;
import com.github.agjacome.httpserver.util.Arrays;
import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.ValuesOf;
import com.sun.net.httpserver.HttpExchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Theories.class)
public class HttpMethodTest {

    @DataPoints
    public static final Set<String> standardMethods = Arrays.asSet(
        "ACL", "BASELINE-CONTROL", "BIND", "CHECKIN", "CHECKOUT", "CONNECT",
        "COPY", "DELETE", "GET", "HEAD", "LABEL", "LINK", "LOCK", "MERGE",
        "MKACTIVITY", "MKCALENDAR", "MKCOL", "MKREDIRECTREF", "MKWORKSPACE",
        "MOVE", "OPTIONS", "ORDERPATCH", "PATCH", "POST", "PRI", "PROPFIND",
        "PROPPATCH", "PUT", "REBIND", "REPORT", "SEARCH", "TRACE", "UNBIND",
        "UNCHECKOUT", "UNLINK", "UNLOCK", "UPDATE", "UPDATEREDIRECTREF",
        "VERSION-CONTROL"
    );

    @Theory
    public void of_must_return_empty_if_method_name_is_not_standard(
        @ForAll final String string
    ) {
        assumeFalse(standardMethods.contains(string));
        assertThat(StandardHttpMethod.of(string)).isEmpty();
    }

    @Theory
    public void of_must_return_present_if_method_name_is_standard(
        final String method
    ) {
        assertThat(StandardHttpMethod.of(method)).isPresent();
    }

    @Theory
    public void get_name_must_return_hyphenated_name(
        @ForAll @ValuesOf final StandardHttpMethod method
    ) {
        assertThat(method.getName()).doesNotContain("_").isIn(standardMethods);
    }

    @Theory
    public void matches_must_match_on_correct_request_method(
        @ForAll @ValuesOf final StandardHttpMethod method
    ) {
        final HttpExchange mockedExchange = mock(HttpExchange.class);

        when(mockedExchange.getRequestMethod()).thenReturn(method.getName());

        assertThat(method.matches(mockedExchange)).isTrue();

        verify(mockedExchange).getRequestMethod();
    }

    @Theory
    public void matches_must_not_match_on_incorrect_request_method(
        @ForAll(sampleSize = 10) final String string,
        @ForAll @ValuesOf final StandardHttpMethod method
    ) {
        assumeFalse(method.getName().equals(string));

        final HttpExchange mockedExchange = mock(HttpExchange.class);

        when(mockedExchange.getRequestMethod()).thenReturn(string);

        assertThat(method.matches(mockedExchange)).isFalse();

        verify(mockedExchange).getRequestMethod();
    }

}

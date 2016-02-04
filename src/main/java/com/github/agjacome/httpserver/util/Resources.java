package com.github.agjacome.httpserver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.agjacome.httpserver.util.annotation.DisallowConstruction;

import static java.lang.Thread.currentThread;
import static java.util.stream.Collectors.joining;

public final class Resources {

    @DisallowConstruction
    private Resources() { }

    public static String readResourceAsString(
        final String path
    ) throws IOException {
        try (final BufferedReader reader = getResourceReader(path)) {
            return reader.lines().collect(joining("\n"));
        }
    }

    public static BufferedReader getResourceReader(final String path) {
        final ClassLoader loader = currentThread().getContextClassLoader();
        return new BufferedReader(new InputStreamReader(
            loader.getResourceAsStream(path)
        ));
    }

}

package com.github.agjacome.httpserver.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.agjacome.httpserver.util.annotation.DisallowConstruction;

import static java.util.Objects.isNull;

public final class URIs {

    @DisallowConstruction
    private URIs() { }

    public static Map<String, List<String>> parseParams(final String query) {
        if (isNull(query)) return Collections.emptyMap();

        final Map<String, List<String>> params = new LinkedHashMap<>();

        // TODO: refactor to use StringTokenizer instead of direct "split"
        for (final String pair : query.split("&")) {
            final int index = pair.indexOf("=");

            final String key = index > 0 ? decodeURLtoUTF8(pair.substring(0, index)) : pair;
            params.putIfAbsent(key, new LinkedList<>());

            final String value = index > 0 && pair.length() > index + 1
                               ? decodeURLtoUTF8(pair.substring(index + 1))
                               : "";

            params.get(key).add(value);
        }

        return params;
    }

    public static Map<String, List<String>> parseParams(final URI uri) {
        return parseParams(uri.getQuery());
    }

    public static String encodeUTF8toURL(final String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new AssertionError("UTF-8 Character encoding not found", uee);
        }
    }

    public static String decodeURLtoUTF8(final String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (final UnsupportedEncodingException uee) {
            throw new AssertionError("UTF-8 Character encoding not found", uee);
        }
    }

}

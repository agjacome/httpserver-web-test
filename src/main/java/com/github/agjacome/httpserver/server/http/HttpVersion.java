package com.github.agjacome.httpserver.server.http;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;

import static java.util.Objects.isNull;

import static com.github.agjacome.httpserver.server.http.HttpVersion.StandardHttpVersion.HTTP_0_9;

// http://tools.ietf.org/html/rfc7230#section-2.6
public interface HttpVersion extends HttpExchangeMatcher {

    public static HttpVersion parse(final String version) {
        // https://tools.ietf.org/html/rfc1945#section-3.1:
        // "If the protocol version is not specified, the recipient must assume
        // that the message is in the simple HTTP/0.9 format."
        if (isNull(version) || version.isEmpty()) return HTTP_0_9;

        final Pattern pattern = Pattern.compile("HTTP/(\\d+).(\\d+)");
        final Matcher matcher = pattern.matcher(version);

        if (!matcher.find()) throw new IllegalArgumentException();

        return new HttpVersion() {
          private final int major = Integer.parseInt(matcher.group(1));
          private final int minor = Integer.parseInt(matcher.group(2));

          @Override public int getMajor() { return major; }
          @Override public int getMinor() { return minor; }
        };
    }

    public int getMajor();

    public int getMinor();

    public default String getName() {
        return String.format("HTTP/%d.%d", getMajor(), getMinor());
    }

    @Override
    public default boolean matches(final HttpExchange exchange) {
        return exchange.getProtocol().equals(getName());
    }


    public static enum StandardHttpVersion implements HttpVersion {

        HTTP_0_9(0, 9),
        HTTP_1_0(1, 0),
        HTTP_1_1(1, 1),
        HTTP_2_0(2, 0);

        private final int major;
        private final int minor;

        public static Optional<HttpVersion> of(final String version) {
            switch (version) {
                case "HTTP/0.9": return Optional.of(HTTP_0_9);
                case "HTTP/1.0": return Optional.of(HTTP_1_0);
                case "HTTP/1.1": return Optional.of(HTTP_1_1);
                case "HTTP/2.0": return Optional.of(HTTP_2_0);
                default:         return Optional.empty();
            }
        }

        private StandardHttpVersion(final int major, final int minor) {
            this.major = major;
            this.minor = minor;
        }

        @Override public int getMajor() { return major; }
        @Override public int getMinor() { return minor; }

    }

}

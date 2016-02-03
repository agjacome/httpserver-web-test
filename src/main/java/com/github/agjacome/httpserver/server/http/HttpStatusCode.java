package com.github.agjacome.httpserver.server.http;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.sun.net.httpserver.HttpExchange;

import static java.util.stream.Collectors.toMap;

// https://tools.ietf.org/html/rfc7231#section-6
public interface HttpStatusCode extends HttpExchangeMatcher {

    public int getCode();

    public String getReason();

    public StatusClass getStatusClass();

    @Override
    default boolean matches(final HttpExchange exchange) {
        return getCode() == exchange.getResponseCode();
    }


    public static enum StatusClass {
        INFORMATIONAL(100, 199),
        SUCCESSFUL(200, 299),
        REDURECTION(300, 399),
        CLIENT_ERROR(400, 499),
        SERVER_ERROR(500, 599);

        private final int minCode;
        private final int maxCode;

        public static Optional<StatusClass> of(final int code) {
            return EnumSet.allOf(StatusClass.class).stream().filter(
              clazz -> clazz.getMinCode() >= code && clazz.getMaxCode() <= code
            ).findFirst();
        }

        private StatusClass(final int minCode, final int maxCode) {
            this.minCode = minCode;
            this.maxCode = maxCode;
        }

        public int getMinCode() { return minCode; }
        public int getMaxCode() { return maxCode; }

    }


    // https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml
    public static enum StandardHttpStatusCode implements HttpStatusCode {

        CONTINUE(100, "Continue"),
        SWITCHING_PROTOCOLS(101, "Switching Protocols"),
        PROCESSING(102, "Processing"),

        OK(200, "OK"),
        CREATED(201, "Created"),
        ACCEPTED(202, "Accepted"),
        NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
        NO_CONTENT(204, "No Content"),
        RESET_CONTENT(205, "Reset Content"),
        PARTIAL_CONTENT(206, "Partial Content"),
        MULTI_STATUS(207, "Multi-Status"),
        ALREADY_REPORTED(208, "Already Reported"),
        IM_USED(226, "IM Used"),

        MULTIPLE_CHOICES(300, "Multiple Choices"),
        MOVED_PERMANENTLY(301, "Moved Permanently"),
        FOUND(302, "Found"),
        SEE_OTHER(303, "See Other"),
        NOT_MODIFIED(304, "Not Modified"),
        USE_PROXY(305, "Use Proxy"),
        TEMPORARY_REDIRECT(307, "Temporary Redirect"),
        PERMANENT_REDIRECT(308, "Permanent Redirect"),

        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        PAYMENT_REQUIRED(402, "Payment Required"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        NOT_ACCEPTABLE(406, "Not Acceptable"),
        PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
        REQUEST_TIMEOUT(408, "Request Timeout"),
        CONFLICT(409, "Conflict"),
        GONE(410, "Gone"),
        LENGTH_REQUIRED(411, "Length Required"),
        PRECONDITION_FAILED(412, "Precondition Failed"),
        PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
        URI_TOO_LONG(414, "URI Too Long"),
        UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
        RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),
        EXPECTATION_FAILED(417, "Expectation Failed"),
        UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
        LOCKED(423, "Locked"),
        FAILED_DEPENDENCY(424, "Failed Dependency"),
        UPGRADE_REQUIRED(426, "Upgrade Required"),
        PRECONDITION_REQUIRED(428, "Precondition Required"),
        TOO_MANY_REQUESTS(429, "Too Many Requests"),
        REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
        UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),

        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        NOT_IMPLEMENTED(501, "Not Implemented"),
        BAD_GATEWAY(502, "Bad Gateway"),
        SERVICE_UNAVAILABLE(503, "Service Unavailable"),
        GATEWAY_TIMEOUT(504, "Gateway Timeout"),
        HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
        VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
        INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
        LOOP_DETECTED(508, "Loop Detected"),
        NOT_EXTENDED(510, "Not Extended"),
        NETWORK_AUTHENTICATION_REQUIRED (511, "Network Authentication Required");

        private final int         code;
        private final String      reason;
        private final StatusClass clazz;

        private static final Map<Integer, HttpStatusCode> codes   = buildHttpStatusMap(HttpStatusCode::getCode);
        private static final Map<String, HttpStatusCode>  reasons = buildHttpStatusMap(HttpStatusCode::getReason);

        private static <A> Map<A, HttpStatusCode> buildHttpStatusMap(
            final Function<HttpStatusCode, A> keyMapper
        ) {
            return EnumSet.allOf(StandardHttpStatusCode.class).stream()
                  .collect(toMap(keyMapper, Function.identity()));
        }

        public static Optional<HttpStatusCode> ofCode(final int code) {
            return Optional.ofNullable(codes.get(code));
        }

        public static Optional<HttpStatusCode> ofReason(final String reason) {
            return Optional.ofNullable(reasons.get(reason));
        }

        private StandardHttpStatusCode(final int code, final String reason) {
            this.code   = code;
            this.reason = reason;
            this.clazz  = StatusClass.of(code).get();
        }

        @Override public int         getCode()        { return code;   }
        @Override public String      getReason()      { return reason; }
        @Override public StatusClass getStatusClass() { return clazz;  }

    }

}
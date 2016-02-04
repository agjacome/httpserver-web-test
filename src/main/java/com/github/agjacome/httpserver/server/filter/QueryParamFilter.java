package com.github.agjacome.httpserver.server.filter;

import com.github.agjacome.httpserver.server.ServerRequest;
import com.github.agjacome.httpserver.server.http.HttpQueryParameter;

import static java.util.Objects.requireNonNull;

public final class QueryParamFilter implements ServerRequestFilter {

    private final HttpQueryParameter queryParam;

    public static ServerRequestFilter queryParam(
        final String key, final String ... values
    ) {
        return queryParam(new HttpQueryParameter(key, values));
    }

    public static ServerRequestFilter queryParam(
        final HttpQueryParameter queryParam
    ) {
        return new QueryParamFilter(queryParam);
    }

    public QueryParamFilter(final HttpQueryParameter queryParam) {
        this.queryParam = requireNonNull(queryParam);
    }

    @Override
    public boolean matches(final ServerRequest request) {
        return queryParam.matches(request.getHttpRequest());
    }

}

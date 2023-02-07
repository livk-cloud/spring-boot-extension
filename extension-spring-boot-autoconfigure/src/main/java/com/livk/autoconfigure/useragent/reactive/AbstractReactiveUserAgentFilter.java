package com.livk.autoconfigure.useragent.reactive;

import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <p>
 * AbstractReactiveUserAgentFilter
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractReactiveUserAgentFilter<T> implements WebFilter {

    private final HttpUserAgentParser<T> userAgentParse;

    @NonNull
    @Override
    public final Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().beforeCommit(() -> Mono.deferContextual(Mono::just)
                .contextWrite(ReactiveUserAgentContextHolder.clearContext())
                .then());
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return chain.filter(exchange)
                .contextWrite(ReactiveUserAgentContextHolder.withContext(userAgentParse.parse(headers)));
    }
}

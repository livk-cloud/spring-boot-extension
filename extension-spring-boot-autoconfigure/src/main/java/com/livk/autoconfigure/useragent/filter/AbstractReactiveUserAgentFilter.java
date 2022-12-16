package com.livk.autoconfigure.useragent.filter;

import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * <p>
 * AbstractReactiveUserAgentFilter
 * </p>
 *
 * @author livk
 * @date 2022/12/15
 */
public abstract class AbstractReactiveUserAgentFilter implements WebFilter {

    @NonNull
    @Override
    public final Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().beforeCommit(() -> Mono.deferContextual(Mono::just)
                .contextWrite(clear())
                .then());
        return chain.filter(exchange)
                .contextWrite(write(exchange));
    }

    protected abstract Context write(ServerWebExchange exchange);

    protected abstract Function<Context, Context> clear();


}

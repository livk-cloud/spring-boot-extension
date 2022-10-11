package com.livk.browscap.filter;

import com.livk.browscap.support.ReactiveUserAgentContext;
import com.livk.browscap.util.UserAgentUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <p>
 * ReactiveUserAgentFilter
 * </p>
 *
 * @author livk
 * @date 2022/9/30
 */
public class ReactiveUserAgentFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .contextWrite(ReactiveUserAgentContext.withContext(UserAgentUtils.parse(exchange.getRequest())))
                .doFinally(signalType -> {
                })
                .contextWrite(ReactiveUserAgentContext.clearContext());
    }
}

package com.livk.browscap.filter;

import com.livk.browscap.support.UserAgentContext;
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
        UserAgentContext.set(UserAgentUtils.parse(exchange.getRequest()));
        return chain.filter(exchange).doFinally(signalType -> UserAgentContext.remove());
    }
}

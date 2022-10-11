package com.livk.browscap.filter;

import com.blueconic.browscap.Capabilities;
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
        Capabilities capabilities = UserAgentUtils.parse(exchange.getRequest());
        exchange.getResponse().beforeCommit(() -> Mono.deferContextual(Mono::just)
                .contextWrite(ReactiveUserAgentContext.clearContext())
                .then());
        return chain.filter(exchange)
                .contextWrite(ReactiveUserAgentContext.withContext(capabilities));
    }
}

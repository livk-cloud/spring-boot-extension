package com.livk.autoconfigure.browscap.filter;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.browscap.support.ReactiveUserAgentContextHolder;
import com.livk.autoconfigure.browscap.util.UserAgentUtils;
import org.springframework.lang.NonNull;
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

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Capabilities capabilities = UserAgentUtils.parse(exchange.getRequest());
        exchange.getResponse().beforeCommit(() -> Mono.deferContextual(Mono::just)
                .contextWrite(ReactiveUserAgentContextHolder.clearContext())
                .then());
        return chain.filter(exchange)
                .contextWrite(ReactiveUserAgentContextHolder.withContext(capabilities));
    }
}

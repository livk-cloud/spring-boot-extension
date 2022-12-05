package com.livk.autoconfigure.useragent.browscap.filter;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.browscap.support.ReactiveUserAgentContextHolder;
import com.livk.autoconfigure.useragent.browscap.util.UserAgentUtils;
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
 *
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

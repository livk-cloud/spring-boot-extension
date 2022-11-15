package com.livk.autoconfigure.yauaa.filter;

import com.livk.autoconfigure.yauaa.support.ReactiveUserAgentContextHolder;
import com.livk.autoconfigure.yauaa.util.UserAgentUtils;
import nl.basjes.parse.useragent.UserAgent;
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
        UserAgent userAgent = UserAgentUtils.parse(exchange.getRequest());
        exchange.getResponse().beforeCommit(() -> Mono.deferContextual(Mono::just)
                .contextWrite(ReactiveUserAgentContextHolder.clearContext())
                .then());
        return chain.filter(exchange)
                .contextWrite(ReactiveUserAgentContextHolder.withContext(userAgent));
    }
}

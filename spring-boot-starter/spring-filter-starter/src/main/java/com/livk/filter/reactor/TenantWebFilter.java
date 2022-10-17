package com.livk.filter.reactor;

import com.livk.filter.context.ReactiveTenantContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <p>
 * Tenant
 * </p>
 *
 * @author livk
 * @date 2022/5/10
 */
public class TenantWebFilter implements WebFilter {

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String tenantId = exchange.getRequest().getHeaders().getFirst(ReactiveTenantContextHolder.ATTRIBUTES);
        exchange.getResponse().beforeCommit(() -> Mono.deferContextual(Mono::just)
                .contextWrite(ReactiveTenantContextHolder.clearContext())
                .then());
        return chain.filter(exchange)
                .contextWrite(ReactiveTenantContextHolder.withContext(tenantId));
    }

}

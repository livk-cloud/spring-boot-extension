package com.livk.filter.reactor;

import com.livk.filter.context.TenantContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * <p>
 * Tenant
 * </p>
 *
 * @author livk
 * @date 2022/5/10
 */
public class TenantWebFilter implements WebFilter {

	@Nonnull
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		TenantContext.setTenantId(exchange.getRequest().getHeaders().getFirst(TenantContext.ATTRIBUTES));
		return chain.filter(exchange).doFinally(signalType -> TenantContext.remove());
	}

}

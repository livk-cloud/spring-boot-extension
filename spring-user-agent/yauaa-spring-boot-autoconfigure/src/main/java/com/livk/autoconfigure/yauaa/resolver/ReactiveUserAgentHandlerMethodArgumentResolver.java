package com.livk.autoconfigure.yauaa.resolver;

import com.livk.autoconfigure.yauaa.annotation.UserAgent;
import com.livk.autoconfigure.yauaa.support.ReactiveUserAgentContextHolder;
import com.livk.autoconfigure.yauaa.util.UserAgentUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * HandlerMethodArgumentResolver
 * </p>
 *
 * @author livk
 * @date 2022/9/30
 */
public class ReactiveUserAgentHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAgent.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
        ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);

        Mono<nl.basjes.parse.useragent.UserAgent> mono = ReactiveUserAgentContextHolder.get()
                .defaultIfEmpty(UserAgentUtils.parse(exchange.getRequest()));
        return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
    }
}


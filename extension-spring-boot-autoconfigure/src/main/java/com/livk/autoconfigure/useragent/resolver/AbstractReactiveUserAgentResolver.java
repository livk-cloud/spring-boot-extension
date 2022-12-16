package com.livk.autoconfigure.useragent.resolver;

import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * AbstractUserAgentHandlerMethodArgumentResolver
 * </p>
 *
 * @author livk
 * @date 2022/12/16
 */
public abstract class AbstractReactiveUserAgentResolver<T> implements HandlerMethodArgumentResolver {

    private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

    @Override
    public final boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAgentInfo.class);
    }

    @NonNull
    @Override
    public final Mono<Object> resolveArgument(@NonNull MethodParameter parameter, @NonNull BindingContext bindingContext, ServerWebExchange exchange) {
        Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
        ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);

        Mono<T> mono = getUserAgent().switchIfEmpty(parseUserAgent(exchange.getRequest()));
        return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
    }

    protected abstract Mono<T> getUserAgent();

    protected abstract Mono<T> parseUserAgent(ServerHttpRequest request);
}

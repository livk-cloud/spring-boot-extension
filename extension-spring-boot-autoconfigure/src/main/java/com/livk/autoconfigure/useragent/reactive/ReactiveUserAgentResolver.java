package com.livk.autoconfigure.useragent.reactive;

import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.ResolvableType;
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
 * @param <T> the type parameter
 * @author livk
 */
@RequiredArgsConstructor
public class ReactiveUserAgentResolver implements HandlerMethodArgumentResolver {

    private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

    private final HttpUserAgentParser userAgentParse;


    @Override
    public final boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAgentInfo.class);
    }

    @NonNull
    @Override
    public final Mono<Object> resolveArgument(@NonNull MethodParameter parameter, @NonNull BindingContext bindingContext, ServerWebExchange exchange) {
        Class<?> resolvedType = ResolvableType.forMethodParameter(parameter).resolve();
        ReactiveAdapter adapter = (resolvedType != null ? adapterRegistry.getAdapter(resolvedType) : null);

        Mono<Object> mono = ReactiveUserAgentContextHolder.get()
                .switchIfEmpty(Mono.justOrEmpty(userAgentParse.parse(exchange.getRequest().getHeaders())))
                .map(wrapper -> {
                    if (adapter != null) {
                        Class<?> type = ResolvableType.forMethodParameter(parameter).resolveGeneric(0);
                        return wrapper.unwrap(type);
                    }
                    return wrapper.unwrap(resolvedType);
                });
        return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
    }
}

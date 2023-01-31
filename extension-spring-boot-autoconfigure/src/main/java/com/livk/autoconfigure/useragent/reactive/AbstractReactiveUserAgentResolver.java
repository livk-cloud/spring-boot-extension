package com.livk.autoconfigure.useragent.reactive;

import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import com.livk.commons.domain.Wrapper;
import com.livk.commons.support.SpringContextHolder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.*;
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
public abstract class AbstractReactiveUserAgentResolver<T> implements HandlerMethodArgumentResolver {

    private final ReactiveAdapterRegistry adapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

    private final HttpUserAgentParser<T> userAgentParse;

    /**
     * Instantiates a new Abstract reactive user agent resolver.
     */
    @SuppressWarnings("unchecked")
    public AbstractReactiveUserAgentResolver() {
        Class<T> annotationClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractReactiveUserAgentResolver.class);
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(HttpUserAgentParser.class, annotationClass);
        ObjectProvider<HttpUserAgentParser<T>> beanProvider = SpringContextHolder.getBeanProvider(resolvableType);
        userAgentParse = beanProvider.getIfAvailable();
    }

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
                .switchIfEmpty(Mono.justOrEmpty((Wrapper<?>) userAgentParse.parse(exchange.getRequest().getHeaders())))
                .map(Wrapper::obj);
        return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
    }
}

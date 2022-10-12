package com.livk.browscap.resolver;

import com.blueconic.browscap.Capabilities;
import com.livk.browscap.annotation.UserAgent;
import com.livk.browscap.support.ReactiveUserAgentContext;
import com.livk.browscap.util.UserAgentUtils;
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

        Mono<Capabilities> mono = ReactiveUserAgentContext.get()
                .defaultIfEmpty(UserAgentUtils.parse(exchange.getRequest()));
        return (adapter != null ? Mono.just(adapter.fromPublisher(mono)) : Mono.from(mono));
    }
}


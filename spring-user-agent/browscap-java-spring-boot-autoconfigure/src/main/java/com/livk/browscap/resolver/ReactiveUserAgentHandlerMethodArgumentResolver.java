package com.livk.browscap.resolver;

import com.blueconic.browscap.Capabilities;
import com.livk.browscap.annotation.UserAgent;
import com.livk.browscap.constant.UserAgentConstant;
import com.livk.browscap.support.UserAgentContext;
import com.livk.browscap.util.UserAgentUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAgent.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        Capabilities capabilities = UserAgentContext.get();
        if (capabilities == null) {
            ServerHttpRequest request = exchange.getRequest();
            String userAgent = request.getHeaders().getFirst(UserAgentConstant.userAgent);
            capabilities = UserAgentUtils.parse(userAgent);
        }
        return Mono.just(capabilities);
    }
}


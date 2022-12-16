package com.livk.autoconfigure.useragent.browscap.resolver;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.browscap.support.ReactiveUserAgentContextHolder;
import com.livk.autoconfigure.useragent.browscap.util.UserAgentUtils;
import com.livk.autoconfigure.useragent.resolver.AbstractReactiveUserAgentResolver;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * <p>
 * HandlerMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
public class ReactiveUserAgentHandlerMethodArgumentResolver extends AbstractReactiveUserAgentResolver<Capabilities> {

    @Override
    protected Mono<Capabilities> getUserAgent() {
        return ReactiveUserAgentContextHolder.get();
    }

    @Override
    protected Mono<Capabilities> parseUserAgent(ServerHttpRequest request) {
        return Mono.just(UserAgentUtils.parse(request));
    }
}


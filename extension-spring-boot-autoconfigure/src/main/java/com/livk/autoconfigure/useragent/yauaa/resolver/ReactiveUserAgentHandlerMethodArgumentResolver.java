package com.livk.autoconfigure.useragent.yauaa.resolver;

import com.livk.autoconfigure.useragent.resolver.AbstractReactiveUserAgentResolver;
import com.livk.autoconfigure.useragent.yauaa.support.ReactiveUserAgentContextHolder;
import com.livk.autoconfigure.useragent.yauaa.util.UserAgentUtils;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * <p>
 * HandlerMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
public class ReactiveUserAgentHandlerMethodArgumentResolver extends AbstractReactiveUserAgentResolver<UserAgent> {
    @Override
    protected Mono<UserAgent> getUserAgent() {
        return ReactiveUserAgentContextHolder.get();
    }

    @Override
    protected Mono<UserAgent> parseUserAgent(ServerHttpRequest request) {
        return Mono.just(UserAgentUtils.parse(request));
    }
}


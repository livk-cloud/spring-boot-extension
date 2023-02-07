package com.livk.autoconfigure.useragent.browscap.resolver;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.reactive.AbstractReactiveUserAgentResolver;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;

/**
 * <p>
 * HandlerMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
public class ReactiveUserAgentHandlerMethodArgumentResolver extends AbstractReactiveUserAgentResolver<Capabilities> {

    /**
     * Instantiates a new Reactive user agent handler method argument resolver.
     *
     * @param userAgentParse the user agent parse
     */
    public ReactiveUserAgentHandlerMethodArgumentResolver(HttpUserAgentParser<Capabilities> userAgentParse) {
        super(userAgentParse);
    }
}


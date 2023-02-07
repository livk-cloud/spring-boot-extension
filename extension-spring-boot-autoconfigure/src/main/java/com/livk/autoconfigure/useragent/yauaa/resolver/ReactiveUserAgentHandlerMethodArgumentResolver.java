package com.livk.autoconfigure.useragent.yauaa.resolver;

import com.livk.autoconfigure.useragent.reactive.AbstractReactiveUserAgentResolver;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import nl.basjes.parse.useragent.UserAgent;

/**
 * <p>
 * HandlerMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
public class ReactiveUserAgentHandlerMethodArgumentResolver extends AbstractReactiveUserAgentResolver<UserAgent> {

    /**
     * Instantiates a new Reactive user agent handler method argument resolver.
     *
     * @param userAgentParse the user agent parse
     */
    public ReactiveUserAgentHandlerMethodArgumentResolver(HttpUserAgentParser<UserAgent> userAgentParse) {
        super(userAgentParse);
    }
}


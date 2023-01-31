package com.livk.autoconfigure.useragent.browscap.filter;

import com.livk.autoconfigure.useragent.reactive.AbstractReactiveUserAgentFilter;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;

/**
 * <p>
 * ReactiveUserAgentFilter
 * </p>
 *
 * @author livk
 */
public class ReactiveUserAgentFilter extends AbstractReactiveUserAgentFilter {

    /**
     * Instantiates a new Reactive user agent filter.
     *
     * @param userAgentParse the user agent parse
     */
    public ReactiveUserAgentFilter(HttpUserAgentParser<?> userAgentParse) {
        super(userAgentParse);
    }
}

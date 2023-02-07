package com.livk.autoconfigure.useragent.browscap.filter;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.reactive.AbstractReactiveUserAgentFilter;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;

/**
 * The type Reactive user agent filter.
 */
public class ReactiveUserAgentFilter extends AbstractReactiveUserAgentFilter<Capabilities> {

    /**
     * Instantiates a new Reactive user agent filter.
     *
     * @param userAgentParse the user agent parse
     */
    public ReactiveUserAgentFilter(HttpUserAgentParser<Capabilities> userAgentParse) {
        super(userAgentParse);
    }
}

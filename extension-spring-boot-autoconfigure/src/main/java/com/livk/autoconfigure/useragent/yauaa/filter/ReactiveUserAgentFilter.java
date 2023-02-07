package com.livk.autoconfigure.useragent.yauaa.filter;

import com.livk.autoconfigure.useragent.reactive.AbstractReactiveUserAgentFilter;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import nl.basjes.parse.useragent.UserAgent;

/**
 * The type Reactive user agent filter.
 *
 * @author livk
 */
public class ReactiveUserAgentFilter extends AbstractReactiveUserAgentFilter<UserAgent> {

    /**
     * Instantiates a new Reactive user agent filter.
     *
     * @param userAgentParse the user agent parse
     */
    public ReactiveUserAgentFilter(HttpUserAgentParser<UserAgent> userAgentParse) {
        super(userAgentParse);
    }
}

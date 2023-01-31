package com.livk.autoconfigure.useragent.yauaa.filter;

import com.livk.autoconfigure.useragent.servlet.AbstractUserAgentFilter;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;

/**
 * <p>
 * UserAgentFilter
 * </p>
 *
 * @author livk
 */
public class UserAgentFilter extends AbstractUserAgentFilter {

    /**
     * Instantiates a new User agent filter.
     *
     * @param userAgentParse the user agent parse
     */
    public UserAgentFilter(HttpUserAgentParser<?> userAgentParse) {
        super(userAgentParse);
    }
}

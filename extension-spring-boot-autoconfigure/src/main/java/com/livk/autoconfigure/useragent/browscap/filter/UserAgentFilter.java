package com.livk.autoconfigure.useragent.browscap.filter;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.servlet.AbstractUserAgentFilter;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;

/**
 * <p>
 * UserAgentFilter
 * </p>
 *
 * @author livk
 */
public class UserAgentFilter extends AbstractUserAgentFilter<Capabilities> {

    /**
     * Instantiates a new User agent filter.
     *
     * @param userAgentParse the user agent parse
     */
    public UserAgentFilter(HttpUserAgentParser<Capabilities> userAgentParse) {
        super(userAgentParse);
    }
}

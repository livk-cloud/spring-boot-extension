package com.livk.autoconfigure.useragent.yauaa.resolver;

import com.livk.autoconfigure.useragent.servlet.AbstractUserAgentResolver;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import nl.basjes.parse.useragent.UserAgent;

/**
 * <p>
 * UserAgent
 * </p>
 *
 * @author livk
 */
public class UserAgentHandlerMethodArgumentResolver extends AbstractUserAgentResolver<UserAgent> {

    /**
     * Instantiates a new User agent handler method argument resolver.
     *
     * @param userAgentParse the user agent parse
     */
    public UserAgentHandlerMethodArgumentResolver(HttpUserAgentParser<UserAgent> userAgentParse) {
        super(userAgentParse);
    }
}

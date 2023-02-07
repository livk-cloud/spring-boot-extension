package com.livk.autoconfigure.useragent.browscap.resolver;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.servlet.AbstractUserAgentResolver;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;

/**
 * <p>
 * UserAgent
 * </p>
 *
 * @author livk
 */
public class UserAgentHandlerMethodArgumentResolver extends AbstractUserAgentResolver<Capabilities> {

    /**
     * Instantiates a new User agent handler method argument resolver.
     *
     * @param userAgentParse the user agent parse
     */
    public UserAgentHandlerMethodArgumentResolver(HttpUserAgentParser<Capabilities> userAgentParse) {
        super(userAgentParse);
    }
}

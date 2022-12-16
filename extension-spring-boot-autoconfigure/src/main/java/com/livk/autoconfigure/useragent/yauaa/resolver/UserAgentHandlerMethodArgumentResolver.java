package com.livk.autoconfigure.useragent.yauaa.resolver;

import com.livk.autoconfigure.useragent.resolver.AbstractUserAgentResolver;
import com.livk.autoconfigure.useragent.yauaa.support.UserAgentContextHolder;
import com.livk.autoconfigure.useragent.yauaa.util.UserAgentUtils;
import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;

/**
 * <p>
 * UserAgent
 * </p>
 *
 * @author livk
 */
public class UserAgentHandlerMethodArgumentResolver extends AbstractUserAgentResolver<UserAgent> {
    @Override
    protected UserAgent getUserAgent() {
        return UserAgentContextHolder.get();
    }

    @Override
    protected UserAgent parseUserAgent(HttpServletRequest request) {
        return UserAgentUtils.parse(request);
    }
}

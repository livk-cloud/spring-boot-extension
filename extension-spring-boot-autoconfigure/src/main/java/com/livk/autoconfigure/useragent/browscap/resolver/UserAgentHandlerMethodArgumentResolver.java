package com.livk.autoconfigure.useragent.browscap.resolver;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.browscap.support.UserAgentContextHolder;
import com.livk.autoconfigure.useragent.browscap.util.UserAgentUtils;
import com.livk.autoconfigure.useragent.resolver.AbstractUserAgentResolver;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * UserAgent
 * </p>
 *
 * @author livk
 */
public class UserAgentHandlerMethodArgumentResolver extends AbstractUserAgentResolver<Capabilities> {

    @Override
    protected Capabilities getUserAgent() {
        return UserAgentContextHolder.get();
    }

    @Override
    protected Capabilities parseUserAgent(HttpServletRequest request) {
        return UserAgentUtils.parse(request);
    }
}

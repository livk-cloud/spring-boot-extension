package com.livk.autoconfigure.useragent.browscap.filter;

import com.livk.autoconfigure.useragent.browscap.support.UserAgentContextHolder;
import com.livk.autoconfigure.useragent.browscap.util.UserAgentUtils;
import com.livk.autoconfigure.useragent.filter.AbstractUserAgentFilter;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * UserAgentFilter
 * </p>
 *
 * @author livk
 */
public class UserAgentFilter extends AbstractUserAgentFilter {

    @Override
    protected void set(HttpServletRequest request) {
        UserAgentContextHolder.set(UserAgentUtils.parse(request));
    }

    @Override
    protected void remove() {
        UserAgentContextHolder.remove();
    }
}

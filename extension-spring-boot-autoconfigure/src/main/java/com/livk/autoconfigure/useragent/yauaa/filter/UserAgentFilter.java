package com.livk.autoconfigure.useragent.yauaa.filter;

import com.livk.autoconfigure.useragent.filter.AbstractUserAgentFilter;
import com.livk.autoconfigure.useragent.yauaa.support.UserAgentContextHolder;
import com.livk.autoconfigure.useragent.yauaa.util.UserAgentUtils;
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

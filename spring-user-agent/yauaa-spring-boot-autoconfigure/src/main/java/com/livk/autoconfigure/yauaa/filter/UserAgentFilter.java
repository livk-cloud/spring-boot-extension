package com.livk.autoconfigure.yauaa.filter;

import com.livk.autoconfigure.yauaa.support.UserAgentContextHolder;
import com.livk.autoconfigure.yauaa.util.UserAgentUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * UserAgentFilter
 * </p>
 *
 * @author livk
 * @date 2022/9/30
 */
public class UserAgentFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        UserAgentContextHolder.set(UserAgentUtils.parse(request));
        filterChain.doFilter(request, response);
        UserAgentContextHolder.remove();
    }
}

package com.livk.browscap.filter;

import com.livk.browscap.support.UserAgentContext;
import com.livk.browscap.util.UserAgentUtils;
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
        UserAgentContext.set(UserAgentUtils.parse(request));
        filterChain.doFilter(request, response);
        UserAgentContext.remove();
    }
}

package com.livk.autoconfigure.useragent.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * AbstractUserAgentFilter
 * </p>
 *
 * @author livk
 * @date 2022/12/15
 */
public abstract class AbstractUserAgentFilter extends OncePerRequestFilter {
    @Override
    protected final void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        set(request);
        filterChain.doFilter(request, response);
        remove();
    }

    protected abstract void set(HttpServletRequest request);

    protected abstract void remove();
}

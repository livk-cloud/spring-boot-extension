package com.livk.autoconfigure.useragent.servlet;

import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import com.livk.commons.bean.domain.Wrapper;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * AbstractUserAgentFilter
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractUserAgentFilter<T> extends OncePerRequestFilter {

    private final HttpUserAgentParser<T> userAgentParse;

    @Override
    protected final void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpHeaders headers = WebUtils.headers(request);
        Wrapper<?> useragentWrapper = userAgentParse.parse(headers);
        UserAgentContextHolder.setUserAgentContext(useragentWrapper);
        filterChain.doFilter(request, response);
        UserAgentContextHolder.cleanUserAgentContext();
    }
}

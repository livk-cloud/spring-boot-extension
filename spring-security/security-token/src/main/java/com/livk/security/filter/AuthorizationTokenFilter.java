package com.livk.security.filter;

import com.livk.commons.util.WebUtils;
import com.livk.security.support.AuthenticationContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * AuthorizationTokenFilter
 * </p>
 *
 * @author livk
 */
@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {

    @Value("${permitAll.url}")
    private String[] permitAllUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!ArrayUtils.contains(permitAllUrl, request.getRequestURI())) {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.hasText(token)) {
                // 切换为redis
                Authentication authentication = AuthenticationContext.getAuthentication(token);
                if (authentication == null) {
                    WebUtils.out(response,
                            Map.of("code", HttpStatus.FORBIDDEN.value(), "msg", "token not available"));
                } else {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                }
            } else {
                WebUtils.out(response, Map.of("code", HttpStatus.FORBIDDEN.value(), "msg", "missing token"));
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}

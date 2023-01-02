package com.livk.spring.factory;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * UUIDFilter
 * </p>
 *
 * @author livk
 * @date 2023/1/2
 */
@Component
@WebFilter(filterName = "UUIDFilter", urlPatterns = "/*")
public class UUIDFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UUIDConTextHolder.set();
        filterChain.doFilter(request, response);
        UUIDConTextHolder.remove();
    }
}

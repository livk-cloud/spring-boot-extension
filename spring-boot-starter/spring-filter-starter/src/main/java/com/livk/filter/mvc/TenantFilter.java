package com.livk.filter.mvc;

import com.livk.filter.FilterAutoConfiguration;
import com.livk.filter.context.TenantContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * <p>
 * 使用bean方式{@link FilterAutoConfiguration#filterRegistrationBean()}
 * </p>
 * <p>
 * 使用注解{@link WebFilter}和{@link org.springframework.boot.web.servlet.ServletComponentScan}
 * </p>
 *
 * @author livk
 * @date 2022/5/10
 */
@Order(1)
@WebFilter(filterName = "tenantFilter", urlPatterns = "/*")
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        TenantContextHolder.setTenantId(((HttpServletRequest) request).getHeader(TenantContextHolder.ATTRIBUTES));
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
        TenantContextHolder.remove();
    }

}

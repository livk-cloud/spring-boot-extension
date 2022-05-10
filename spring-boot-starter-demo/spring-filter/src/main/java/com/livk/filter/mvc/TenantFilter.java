package com.livk.filter.mvc;

import com.livk.filter.FilterAutoConfiguration;
import com.livk.filter.context.TenantContext;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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
        TenantContext.setTenantId(((HttpServletRequest) request).getHeader(TenantContext.ATTRIBUTES));
        chain.doFilter(request, response);
        TenantContext.remove();
    }

}

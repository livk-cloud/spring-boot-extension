package com.livk.filter;

import com.livk.filter.mvc.TenantFilter;
import com.livk.filter.reactor.TenantWebFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * FilterAutoConfiguration
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
public class FilterAutoConfiguration {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<TenantFilter> filterRegistrationBean() {
        FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("tenantFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public TenantWebFilter tenantWebFilter() {
        return new TenantWebFilter();
    }

}

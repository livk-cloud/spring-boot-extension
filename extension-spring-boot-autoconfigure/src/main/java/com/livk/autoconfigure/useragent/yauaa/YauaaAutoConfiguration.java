package com.livk.autoconfigure.useragent.yauaa;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.useragent.yauaa.filter.ReactiveUserAgentFilter;
import com.livk.autoconfigure.useragent.yauaa.filter.UserAgentFilter;
import com.livk.autoconfigure.useragent.yauaa.resolver.ReactiveUserAgentHandlerMethodArgumentResolver;
import com.livk.autoconfigure.useragent.yauaa.resolver.UserAgentHandlerMethodArgumentResolver;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * YauaaAutoConfiguration
 * </p>
 *
 * @author livk
 *
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(UserAgentAnalyzer.class)
public class YauaaAutoConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withCache(10000)
                .build();
    }


    @AutoConfiguration(after = YauaaAutoConfiguration.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class YauaaMvcAutoConfiguration implements WebMvcConfigurer {
        @Bean
        public FilterRegistrationBean<UserAgentFilter> filterRegistrationBean() {
            FilterRegistrationBean<UserAgentFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new UserAgentFilter());
            registrationBean.addUrlPatterns("/*");
            registrationBean.setName("userAgentFilter");
            registrationBean.setOrder(1);
            return registrationBean;
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new UserAgentHandlerMethodArgumentResolver());
        }
    }

    @AutoConfiguration(after = YauaaAutoConfiguration.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public static class YauaaReactiveAutoConfiguration implements WebFluxConfigurer {

        @Bean
        public ReactiveUserAgentFilter tenantWebFilter() {
            return new ReactiveUserAgentFilter();
        }

        @Override
        public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
            configurer.addCustomResolver(new ReactiveUserAgentHandlerMethodArgumentResolver());
        }
    }

}

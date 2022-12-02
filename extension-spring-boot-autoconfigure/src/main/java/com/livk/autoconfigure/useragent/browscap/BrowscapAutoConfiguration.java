package com.livk.autoconfigure.useragent.browscap;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.livk.autoconfigure.useragent.browscap.filter.ReactiveUserAgentFilter;
import com.livk.autoconfigure.useragent.browscap.filter.UserAgentFilter;
import com.livk.autoconfigure.useragent.browscap.resolver.ReactiveUserAgentHandlerMethodArgumentResolver;
import com.livk.autoconfigure.useragent.browscap.resolver.UserAgentHandlerMethodArgumentResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * BrowscapAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/9/30
 */
@AutoConfiguration
@ConditionalOnClass(UserAgentParser.class)
public class BrowscapAutoConfiguration {

    @Bean
    public UserAgentParser userAgentParser() throws IOException, ParseException {
        return new UserAgentService().loadParser(Arrays.asList(BrowsCapField.values()));
    }

    @AutoConfiguration(after = BrowscapAutoConfiguration.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class BrowscapMvcAutoConfiguration implements WebMvcConfigurer {
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

    @AutoConfiguration(after = BrowscapAutoConfiguration.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public static class BrowscapReactiveAutoConfiguration implements WebFluxConfigurer {

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

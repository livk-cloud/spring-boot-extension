package com.livk.autoconfigure.useragent.yauaa;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.useragent.yauaa.filter.ReactiveUserAgentFilter;
import com.livk.autoconfigure.useragent.yauaa.filter.UserAgentFilter;
import com.livk.autoconfigure.useragent.yauaa.resolver.ReactiveUserAgentHandlerMethodArgumentResolver;
import com.livk.autoconfigure.useragent.yauaa.resolver.UserAgentHandlerMethodArgumentResolver;
import com.livk.autoconfigure.useragent.yauaa.support.YauaaUserAgentParse;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
 * The type Yauaa auto configuration.
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(UserAgentAnalyzer.class)
public class YauaaAutoConfiguration {

    /**
     * User agent analyzer user agent analyzer.
     *
     * @return the user agent analyzer
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withCache(10000)
                .build();
    }

    /**
     * Yauaa user agent parse yauaa user agent parse.
     *
     * @param userAgentAnalyzer the user agent analyzer
     * @return the yauaa user agent parse
     */
    @Bean
    @ConditionalOnMissingBean
    public YauaaUserAgentParse yauaaUserAgentParse(UserAgentAnalyzer userAgentAnalyzer) {
        return new YauaaUserAgentParse(userAgentAnalyzer);
    }


    /**
     * The type Yauaa mvc auto configuration.
     */
    @RequiredArgsConstructor
    @AutoConfiguration(after = YauaaAutoConfiguration.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class YauaaMvcAutoConfiguration implements WebMvcConfigurer {

        private final YauaaUserAgentParse userAgentParse;

        /**
         * Filter registration bean filter registration bean.
         *
         * @return the filter registration bean
         */
        @Bean
        public FilterRegistrationBean<UserAgentFilter> filterRegistrationBean() {
            FilterRegistrationBean<UserAgentFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new UserAgentFilter(userAgentParse));
            registrationBean.addUrlPatterns("/*");
            registrationBean.setName("userAgentFilter");
            registrationBean.setOrder(1);
            return registrationBean;
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new UserAgentHandlerMethodArgumentResolver(userAgentParse));
        }
    }

    /**
     * The type Yauaa reactive auto configuration.
     */
    @RequiredArgsConstructor
    @AutoConfiguration(after = YauaaAutoConfiguration.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public static class YauaaReactiveAutoConfiguration implements WebFluxConfigurer {

        private final YauaaUserAgentParse userAgentParse;

        /**
         * Reactive user agent filter reactive user agent filter.
         *
         * @return the reactive user agent filter
         */
        @Bean
        public ReactiveUserAgentFilter reactiveUserAgentFilter() {
            return new ReactiveUserAgentFilter(userAgentParse);
        }

        @Override
        public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
            configurer.addCustomResolver(new ReactiveUserAgentHandlerMethodArgumentResolver(userAgentParse));
        }
    }

}

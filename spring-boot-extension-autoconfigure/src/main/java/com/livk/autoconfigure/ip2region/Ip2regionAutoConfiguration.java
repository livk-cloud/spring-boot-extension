/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.ip2region;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.ip2region.filter.RequestIpFilter;
import com.livk.autoconfigure.ip2region.support.Ip2RegionSearch;
import com.livk.autoconfigure.ip2region.support.RequestIPMethodArgumentResolver;
import com.livk.commons.io.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * The type Ip 2 region auto configuration.
 */
@AutoConfiguration
@SpringAutoService
@EnableConfigurationProperties(Ip2RegionProperties.class)
@ConditionalOnProperty(prefix = Ip2RegionProperties.PREFIX, name = "enabled", havingValue = "true")
public class Ip2regionAutoConfiguration {

    /**
     * Ip 2 region search ip 2 region search.
     *
     * @param properties the properties
     * @return the ip 2 region search
     */
    @Bean
    @SneakyThrows
    public Ip2RegionSearch ip2RegionSearch(Ip2RegionProperties properties) {
        Resource resource = properties.getFileResource();
        byte[] bytes = FileUtils.copyToByteArray(resource.getInputStream());
        Searcher searcher = Searcher.newWithBuffer(bytes);
        return new Ip2RegionSearch(searcher);
    }

    /**
     * The type Web mvc ip 2 region auto configuration.
     */
    @AutoConfiguration
    @RequiredArgsConstructor
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class WebMvcIp2regionAutoConfiguration implements WebMvcConfigurer {

        private final Ip2RegionSearch search;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new RequestIPMethodArgumentResolver(search));
        }

        /**
         * Request ip bean factory processor request ip bean factory processor.
         *
         * @return the request ip bean factory processor
         */
        @Bean
        public RequestIpBeanFactoryProcessor requestIpBeanFactoryProcessor() {
            return new RequestIpBeanFactoryProcessor();
        }

        /**
         * Ip filter filter registration bean filter registration bean.
         *
         * @return the filter registration bean
         */
        @Bean
        public FilterRegistrationBean<RequestIpFilter> ipFilterFilterRegistrationBean() {
            FilterRegistrationBean<RequestIpFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new RequestIpFilter(search));
            registrationBean.addUrlPatterns("/*");
            registrationBean.setName("requestIpFilter");
            registrationBean.setOrder(1);
            return registrationBean;
        }
    }
}

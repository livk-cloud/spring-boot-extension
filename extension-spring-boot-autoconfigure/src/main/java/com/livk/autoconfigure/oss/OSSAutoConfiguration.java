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

package com.livk.autoconfigure.oss;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.oss.support.AbstractService;
import com.livk.autoconfigure.oss.support.OSSTemplate;
import com.livk.autoconfigure.oss.support.minio.MinioService;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * The type Oss auto configuration.
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(name = "com.livk.oss.marker.OSSMarker")
@EnableConfigurationProperties(OSSProperties.class)
public class OSSAutoConfiguration {

    /**
     * Oss template oss template.
     *
     * @param abstractService the abstract service
     * @return the oss template
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(AbstractService.class)
    public OSSTemplate ossTemplate(AbstractService<?> abstractService) {
        return new OSSTemplate(abstractService);
    }

    /**
     * The type Minio oss auto configuration.
     */
    @AutoConfiguration
    @ConditionalOnClass(MinioClient.class)
    public static class MinioOSSAutoConfiguration {

        /**
         * Minio service minio service.
         *
         * @param properties the properties
         * @return the minio service
         */
        @Bean(destroyMethod = "close")
        @ConditionalOnMissingBean(AbstractService.class)
        public MinioService minioService(OSSProperties properties) {
            return new MinioService(properties);
        }
    }
}

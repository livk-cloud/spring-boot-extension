package com.livk.autoconfigure.oss;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.oss.client.MinioClientFactory;
import com.livk.autoconfigure.oss.support.AbstractService;
import com.livk.autoconfigure.oss.support.OSSTemplate;
import com.livk.autoconfigure.oss.support.minio.MinioService;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * The type Oss auto configuration.
 *
 * @author livk
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
         * Minio client factory minio client factory.
         *
         * @return the minio client factory
         */
        @Bean
        public MinioClientFactory minioClientFactory() {
            return new MinioClientFactory();
        }

        /**
         * Minio service minio service.
         *
         * @param properties         the properties
         * @param minioClientFactory the minio client factory
         * @return the minio service
         */
        @Bean(destroyMethod = "close")
        public MinioService minioService(OSSProperties properties,
                                         MinioClientFactory minioClientFactory) {
            return new MinioService(properties, minioClientFactory);
        }
    }
}

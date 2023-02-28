package com.livk.autoconfigure.ip2region;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.ip2region.support.IPMethodArgumentResolver;
import com.livk.autoconfigure.ip2region.support.Ip2RegionSearch;
import com.livk.autoconfigure.ip2region.support.RequestIPMethodArgumentResolver;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * Auto
 * </p>
 *
 * @author livk
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
    public Ip2RegionSearch ip2RegionSearch(Ip2RegionProperties properties) {
        try {
            Resource resource = properties.getFileResource();
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            Searcher searcher = Searcher.newWithBuffer(bytes);
            return new Ip2RegionSearch(searcher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The type Web mvc ip 2 region auto configuration.
     */
    @AutoConfiguration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class WebMvcIp2regionAutoConfiguration implements WebMvcConfigurer {
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new IPMethodArgumentResolver());
            resolvers.add(new RequestIPMethodArgumentResolver());
        }
    }
}

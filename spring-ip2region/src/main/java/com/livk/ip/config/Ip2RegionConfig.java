package com.livk.ip.config;

import com.livk.ip.properties.Ip2RegionProperties;
import com.livk.ip.support.Ip2RegionSearch;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 * <p>
 * Ip2RegionConfig
 * </p>
 *
 * @author livk
 * @date 2022/8/18
 */
@Configuration
@EnableConfigurationProperties(Ip2RegionProperties.class)
@ConditionalOnProperty(prefix = Ip2RegionProperties.PREFIX, name = "enabled", havingValue = "true")
public class Ip2RegionConfig {

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

}

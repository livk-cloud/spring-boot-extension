package com.livk.redisson;

import com.livk.redisson.util.YamlUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * RedissonAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/9/16
 */
@RequiredArgsConstructor
@ConditionalOnClass(Redisson.class)
@AutoConfiguration(before = RedisAutoConfiguration.class)
public class RedissonAutoConfiguration implements EnvironmentAware, InitializingBean {
    private static final String REDISSON_CONFIG = "spring.redisson.config.";
    private final List<RedissonAutoConfigurationCustomizer> redissonAutoConfigurationCustomizers;
    private StandardEnvironment environment;
    private Config config;

    @Bean
    @ConditionalOnMissingBean(RedissonReactiveClient.class)
    public RedissonReactiveClient redissonReactive(RedissonClient redisson) {
        return redisson.reactive();
    }

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient() {
        return Redisson.create(config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() {
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> redissonMap = new HashMap<>();
        for (PropertySource<?> propertySource : propertySources) {
            if (propertySource.getSource() instanceof Map) {
                Map<String, Object> source = (Map<String, Object>) propertySource.getSource();
                for (Map.Entry<String, Object> entry : source.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith(REDISSON_CONFIG)) {
                        key = key.replaceFirst(REDISSON_CONFIG, "");
                        redissonMap.put(key, entry.getValue().toString());
                    }
                }
            }
        }
        String redissonYaml = YamlUtils.mapToYml(redissonMap);
        if (StringUtils.hasText(redissonYaml)) {
            try {
                config = Config.fromYAML(redissonYaml);
                if (!CollectionUtils.isEmpty(redissonAutoConfigurationCustomizers)) {
                    for (RedissonAutoConfigurationCustomizer customizer : redissonAutoConfigurationCustomizers) {
                        customizer.customize(config);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (StandardEnvironment) environment;
    }
}

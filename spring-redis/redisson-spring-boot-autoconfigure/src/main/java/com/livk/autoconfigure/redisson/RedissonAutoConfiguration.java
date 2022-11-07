package com.livk.autoconfigure.redisson;

import com.livk.util.SpringUtils;
import com.livk.util.YamlUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.RedisException;
import org.redisson.config.Config;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
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
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonAutoConfiguration implements EnvironmentAware {
    private static final String REDISSON_CONFIG = "spring.redisson.config";

    private static final String REDIS_PROTOCOL_PREFIX = "redis://";

    private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

    private final ObjectProvider<ConfigCustomizer> configCustomizers;

    private StandardEnvironment environment;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnMissingBean(RedissonReactiveClient.class)
    public RedissonReactiveClient redissonReactive(RedissonClient redisson) {
        return redisson.reactive();
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Map<String, String> redissonProperties = SpringUtils.getSubProperties(environment, REDISSON_CONFIG);
        String redissonYaml = YamlUtils.mapToYml(redissonProperties).replaceAll("'", "");
        Config config;
        Duration duration = redisProperties.getTimeout();
        int timeout = duration == null ? 0 : (int) duration.toMillis();
        if (StringUtils.hasText(redissonYaml)) {
            try {
                config = Config.fromYAML(redissonYaml);
            } catch (IOException e) {
                throw new RedisException(e);
            }
        } else if (redisProperties.getSentinel() != null) {
            List<String> nodeList = redisProperties.getSentinel().getNodes();
            String[] nodes = convert(nodeList);
            config = new Config();
            config.useSentinelServers()
                    .setMasterName(redisProperties.getSentinel().getMaster())
                    .addSentinelAddress(nodes)
                    .setDatabase(redisProperties.getDatabase())
                    .setConnectTimeout(timeout)
                    .setPassword(redisProperties.getPassword());
        } else if (redisProperties.getCluster() != null) {
            List<String> nodeList = redisProperties.getCluster().getNodes();
            String[] nodes = convert(nodeList);
            config = new Config();
            config.useClusterServers()
                    .addNodeAddress(nodes)
                    .setConnectTimeout(timeout)
                    .setPassword(redisProperties.getPassword());
        } else {
            config = new Config();
            String prefix = REDIS_PROTOCOL_PREFIX;
            if (redisProperties.isSsl()) {
                prefix = REDISS_PROTOCOL_PREFIX;
            }
            config.useSingleServer()
                    .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                    .setConnectTimeout(timeout)
                    .setDatabase(redisProperties.getDatabase())
                    .setPassword(redisProperties.getPassword());
        }
        configCustomizers.forEach(customizer -> customizer.customize(config));
        return Redisson.create(config);
    }

    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
                nodes.add(REDIS_PROTOCOL_PREFIX + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[0]);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (StandardEnvironment) environment;
    }
}

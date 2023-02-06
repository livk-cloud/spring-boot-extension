package com.livk.autoconfigure.redisearch;

import com.livk.auto.service.annotation.SpringAutoService;
import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import io.lettuce.core.RedisCredentials;
import io.lettuce.core.RedisCredentialsProvider;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * The type Redi search auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(RedisURI.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RediSearchAutoConfiguration {

    /**
     * Configure generic object pool config.
     *
     * @param <K>             the type parameter
     * @param <V>             the type parameter
     * @param redisProperties the redis properties
     * @param config          the config
     * @return the generic object pool config
     */
    public static <K, V> GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> configure(RedisProperties redisProperties,
                                                                                                 GenericObjectPoolConfig<StatefulRedisModulesConnection<K, V>> config) {
        config.setJmxEnabled(false);
        RedisProperties.Pool lettucePool = redisProperties.getLettuce().getPool();
        RedisProperties.Pool jedisPool = redisProperties.getJedis().getPool();
        if (lettucePool != null) {
            config.setMaxTotal(lettucePool.getMaxActive());
            config.setMaxIdle(lettucePool.getMaxIdle());
            config.setMinIdle(lettucePool.getMinIdle());
            if (lettucePool.getMaxWait() != null) {
                config.setMaxWait(lettucePool.getMaxWait());
            }
        } else if (jedisPool != null) {
            config.setMaxTotal(jedisPool.getMaxActive());
            config.setMaxIdle(jedisPool.getMaxIdle());
            config.setMinIdle(jedisPool.getMinIdle());
            if (jedisPool.getMaxWait() != null) {
                config.setMaxWait(jedisPool.getMaxWait());
            }
        }
        return config;
    }

    /**
     * Redis uri redis uri.
     *
     * @param properties          the properties
     * @param redisURICustomizers the redis uri customizers
     * @return the redis uri
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisURI redisURI(RedisProperties properties,
                             ObjectProvider<RedisURICustomizer> redisURICustomizers) {
        RedisURI redisURI = new RedisURI();
        redisURI.setHost(properties.getHost());
        redisURI.setPort(properties.getPort());
        redisURI.setCredentialsProvider(RedisCredentialsProvider.from(
                () -> RedisCredentials.just(properties.getUsername(), properties.getPassword())));
        redisURI.setDatabase(properties.getDatabase());
        Duration timeout = properties.getTimeout();
        if (timeout != null) {
            redisURI.setTimeout(timeout);
        }
        redisURICustomizers.orderedStream().forEach(customizer -> customizer.customize(redisURI));
        return redisURI;
    }

    /**
     * Client resources client resources.
     *
     * @return the client resources
     */
    @Bean(destroyMethod = "shutdown")
    public ClientResources clientResources() {
        return DefaultClientResources.create();
    }

    /**
     * Client redis modules client.
     *
     * @param redisURI        the redis uri
     * @param clientResources the client resources
     * @return the redis modules client
     */
    @Bean(destroyMethod = "shutdown")
    public RedisModulesClient client(RedisURI redisURI, ClientResources clientResources) {
        return RedisModulesClient.create(clientResources, redisURI);
    }

    /**
     * Connection stateful redis modules connection.
     *
     * @param redisModulesClient the redis modules client
     * @return the stateful redis modules connection
     */
    @Bean(name = "redisModulesConnection", destroyMethod = "close")
    StatefulRedisModulesConnection<String, String> connection(RedisModulesClient redisModulesClient) {
        return redisModulesClient.connect();
    }

    /**
     * Pool config generic object pool config.
     *
     * @param redisProperties the redis properties
     * @return the generic object pool config
     */
    @Bean(name = "redisModulesConnectionPoolConfig")
    public GenericObjectPoolConfig<StatefulRedisModulesConnection<String, String>> poolConfig(RedisProperties redisProperties) {
        return configure(redisProperties, new GenericObjectPoolConfig<>());
    }

    /**
     * Pool generic object pool.
     *
     * @param client the client
     * @param config the config
     * @return the generic object pool
     */
    @Bean(name = "redisModulesConnectionPool", destroyMethod = "close")
    public GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool(RedisModulesClient client,
                                                                                  GenericObjectPoolConfig<StatefulRedisModulesConnection<String, String>> config) {
        return ConnectionPoolSupport.createGenericObjectPool(client::connect, config);
    }
}

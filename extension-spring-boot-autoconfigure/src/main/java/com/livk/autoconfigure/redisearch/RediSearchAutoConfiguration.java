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
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * The type RediSearch autoconfiguration.
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(RedisModulesClient.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RediSearchAutoConfiguration {

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
     * @param clientResources the client resources
     * @param properties      the properties
     * @return the redis modules client
     */
    @Bean(destroyMethod = "shutdown")
    public RedisModulesClient client(ClientResources clientResources,
                                     RedisProperties properties) {
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
        redisURI.setSsl(properties.isSsl());
        redisURI.setClientName(properties.getClientName());
        return RedisModulesClient.create(clientResources, redisURI);
    }

    /**
     * Connection stateful redis modules connection.
     *
     * @param redisModulesClient the redis modules client
     * @return the stateful redis modules connection
     */
    @Bean(name = "redisModulesConnection", destroyMethod = "close")
    public StatefulRedisModulesConnection<String, String> connection(RedisModulesClient redisModulesClient) {
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
        GenericObjectPoolConfig<StatefulRedisModulesConnection<String, String>> config = new GenericObjectPoolConfig<>();
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

package com.livk.autoconfigure.redis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.redis.supprot.UniversalReactiveRedisTemplate;
import com.livk.autoconfigure.redis.supprot.UniversalRedisTemplate;
import com.livk.autoconfigure.redis.util.SerializerUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import reactor.core.publisher.Flux;

/**
 * The type Customize redis auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@ConditionalOnClass(RedisOperations.class)
@AutoConfiguration(before = {RedisAutoConfiguration.class})
public class CustomizeRedisAutoConfiguration {

    /**
     * Livk redis template universal redis template.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the universal redis template
     */
    @Bean
    public UniversalRedisTemplate livkRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new UniversalRedisTemplate(redisConnectionFactory);
    }


    /**
     * The type Customize reactive redis auto configuration.
     */
    @AutoConfiguration(before = {RedisReactiveAutoConfiguration.class})
    @ConditionalOnClass({ReactiveRedisConnectionFactory.class, ReactiveRedisTemplate.class, Flux.class})
    public static class CustomizeReactiveRedisAutoConfiguration {

        /**
         * Universal reactive redis template universal reactive redis template.
         *
         * @param redisConnectionFactory the redis connection factory
         * @return the universal reactive redis template
         */
        @Bean
        public UniversalReactiveRedisTemplate universalReactiveRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory) {
            RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext.
                    <String, Object>newSerializationContext()
                    .key(RedisSerializer.string()).value(SerializerUtils.json())
                    .hashKey(RedisSerializer.string()).hashValue(SerializerUtils.json()).build();
            return new UniversalReactiveRedisTemplate(redisConnectionFactory, serializationContext);
        }
    }

}

package com.livk.autoconfigure.redis;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.redis.supprot.LivkReactiveRedisTemplate;
import com.livk.autoconfigure.redis.supprot.LivkRedisTemplate;
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
 * <p>
 * LivkRedisAutoConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService
@ConditionalOnClass(RedisOperations.class)
@AutoConfiguration(before = {RedisAutoConfiguration.class})
public class LivkRedisAutoConfiguration {

    /**
     * Livk redis template livk redis template.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the livk redis template
     */
    @Bean
    public LivkRedisTemplate livkRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new LivkRedisTemplate(redisConnectionFactory);
    }


    /**
     * The type Livk reactive redis auto configuration.
     */
    @AutoConfiguration(before = {RedisReactiveAutoConfiguration.class})
    @ConditionalOnClass({ReactiveRedisConnectionFactory.class, ReactiveRedisTemplate.class, Flux.class})
    public static class LivkReactiveRedisAutoConfiguration {
        /**
         * Livk reactive redis template livk reactive redis template.
         *
         * @param redisConnectionFactory the redis connection factory
         * @return the livk reactive redis template
         */
        @Bean
        public LivkReactiveRedisTemplate livkReactiveRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory) {
            RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext.
                    <String, Object>newSerializationContext()
                    .key(RedisSerializer.string()).value(SerializerUtils.json())
                    .hashKey(RedisSerializer.string()).hashValue(SerializerUtils.json()).build();
            return new LivkReactiveRedisTemplate(redisConnectionFactory, serializationContext);
        }
    }

}

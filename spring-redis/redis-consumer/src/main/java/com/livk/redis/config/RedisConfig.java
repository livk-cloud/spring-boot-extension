package com.livk.redis.config;

import com.livk.common.redis.domain.LivkMessage;
import com.livk.common.redis.util.SerializerUtils;
import com.livk.redis.listener.KeyExpiredListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * <p>
 * RedisConfig
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
@Slf4j
@Configuration
@ConditionalOnClass(RedisOperations.class)
public class RedisConfig {

    @Bean
    public ReactiveRedisMessageListenerContainer reactiveRedisMessageListenerContainer(
            ReactiveRedisConnectionFactory connectionFactory) {
        var container = new ReactiveRedisMessageListenerContainer(connectionFactory);
        var serializer = SerializerUtils.getSerializer(LivkMessage.class);
        container
                .receive(List.of(PatternTopic.of(LivkMessage.CHANNEL)),
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()),
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .map(ReactiveSubscription.Message::getMessage).subscribe(obj -> log.info("message:{}", obj));
        return container;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory) {
        var listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(factory);
        return listenerContainer;
    }

    @Bean
    public KeyExpiredListener keyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        return new KeyExpiredListener(listenerContainer);
    }

}

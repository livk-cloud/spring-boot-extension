package com.livk.common.redis.supprot;

import com.livk.common.redis.util.SerializerUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * <p>
 * LivkRedisTemplate
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
public class LivkRedisTemplate extends RedisTemplate<String, Object> {

	public LivkRedisTemplate() {
		var serializer = SerializerUtils.getSerializer(Object.class);
		this.setKeySerializer(RedisSerializer.string());
		this.setHashKeySerializer(RedisSerializer.string());
		this.setValueSerializer(serializer);
		this.setHashValueSerializer(serializer);
	}

	public LivkRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		this();
		this.setConnectionFactory(redisConnectionFactory);
		this.afterPropertiesSet();
	}

}

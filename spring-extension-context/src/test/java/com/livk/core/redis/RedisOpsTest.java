package com.livk.core.redis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * RedisOpsTest
 * </p>
 *
 * @author livk
 */
@ContextConfiguration(classes = { RedisFactoryConfig.class })
@ExtendWith(SpringExtension.class)
class RedisOpsTest {

	@Autowired
	RedisConnectionFactory connectionFactory;

	@Test
	void test() {
		RedisOps ops = new RedisOps(connectionFactory);
		assertEquals("PONG", ops.execute(RedisConnectionCommands::ping));
	}

}

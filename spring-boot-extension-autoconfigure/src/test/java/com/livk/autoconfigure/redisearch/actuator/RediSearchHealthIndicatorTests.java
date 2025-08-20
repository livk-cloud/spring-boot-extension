/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.redisearch.actuator;

import com.livk.context.redisearch.RediSearchConnectionFactory;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.cluster.api.StatefulRedisModulesClusterConnection;
import com.redis.lettucemod.cluster.api.sync.RedisModulesAdvancedClusterCommands;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.redis.RedisConnectionFailureException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class RediSearchHealthIndicatorTests {

	static String redisVersion = "8.2.0";

	@Test
	@SuppressWarnings("unchecked")
	void redisIsUp() {
		String info = "redis_version:" + redisVersion;
		RedisModulesCommands<String, String> commands = mock(RedisModulesCommands.class);
		StatefulRedisModulesConnection<String, String> connection = mock(StatefulRedisModulesConnection.class);
		given(connection.sync()).willReturn(commands);
		given(commands.info("server")).willReturn(info);
		RediSearchHealthIndicator healthIndicator = createHealthIndicator(connection);
		Health health = healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).containsEntry("version", redisVersion);
	}

	@Test
	@SuppressWarnings("unchecked")
	void redisIsDown() {
		RedisModulesCommands<String, String> commands = mock(RedisModulesCommands.class);
		StatefulRedisModulesConnection<String, String> connection = mock(StatefulRedisModulesConnection.class);
		given(connection.sync()).willReturn(commands);
		given(commands.info("server")).willThrow(new RedisConnectionFailureException("Connection failed"));
		RediSearchHealthIndicator healthIndicator = createHealthIndicator(connection);
		Health health = healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat((String) health.getDetails().get("error")).contains("Connection failed");
	}

	@Test
	void healthWhenClusterStateIsAbsentShouldBeUp() {
		RediSearchConnectionFactory connectionFactory = createClusterConnectionFactory(null);
		RediSearchHealthIndicator healthIndicator = new RediSearchHealthIndicator(connectionFactory);
		Health health = healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).containsEntry("cluster_size", 4);
		assertThat(health.getDetails()).containsEntry("slots_ok", 4);
		assertThat(health.getDetails()).containsEntry("slots_fail", 0);
		then(connectionFactory).should(atLeastOnce()).connect();
	}

	@Test
	void healthWhenClusterStateIsOkShouldBeUp() {
		RediSearchConnectionFactory connectionFactory = createClusterConnectionFactory("ok");
		RediSearchHealthIndicator healthIndicator = new RediSearchHealthIndicator(connectionFactory);
		Health health = healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails()).containsEntry("cluster_size", 4);
		assertThat(health.getDetails()).containsEntry("slots_ok", 4);
		assertThat(health.getDetails()).containsEntry("slots_fail", 0);
		then(connectionFactory).should(atLeastOnce()).connect();
	}

	@Test
	void healthWhenClusterStateIsFailShouldBeDown() {
		RediSearchConnectionFactory connectionFactory = createClusterConnectionFactory("fail");
		RediSearchHealthIndicator healthIndicator = new RediSearchHealthIndicator(connectionFactory);
		Health health = healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails()).containsEntry("cluster_size", 4);
		assertThat(health.getDetails()).containsEntry("slots_ok", 3);
		assertThat(health.getDetails()).containsEntry("slots_fail", 1);
		then(connectionFactory).should(atLeastOnce()).connect();
	}

	private RediSearchHealthIndicator createHealthIndicator(StatefulRedisModulesConnection<String, String> connection) {
		RediSearchConnectionFactory factory = mock(RediSearchConnectionFactory.class);
		given(factory.connect()).willReturn(connection);
		return new RediSearchHealthIndicator(factory);
	}

	@SuppressWarnings("unchecked")
	private RediSearchConnectionFactory createClusterConnectionFactory(String state) {
		StringBuilder clusterInfo = new StringBuilder();
		if (state != null) {
			clusterInfo.append("cluster_state").append(":").append(state).append("\n");
		}
		clusterInfo.append("cluster_size").append(":").append(4).append("\n");
		boolean failure = "fail".equals(state);
		clusterInfo.append("cluster_slots_ok").append(":").append(failure ? 3 : 4).append("\n");
		clusterInfo.append("cluster_slots_fail").append(":").append(failure ? 1 : 0).append("\n");

		RedisModulesAdvancedClusterCommands<String, String> commands = mock(RedisModulesAdvancedClusterCommands.class);
		given(commands.clusterInfo()).willReturn(clusterInfo.toString());
		StatefulRedisModulesClusterConnection<String, String> connection = mock(
				StatefulRedisModulesClusterConnection.class);
		given(connection.sync()).willReturn(commands);
		RediSearchConnectionFactory redisConnectionFactory = mock(RediSearchConnectionFactory.class);
		given(redisConnectionFactory.connect()).willReturn(connection);
		return redisConnectionFactory;
	}

}

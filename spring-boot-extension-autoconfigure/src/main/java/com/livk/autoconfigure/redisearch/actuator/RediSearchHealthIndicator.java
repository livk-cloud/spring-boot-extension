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
import com.redis.lettucemod.cluster.api.StatefulRedisModulesClusterConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class RediSearchHealthIndicator extends AbstractHealthIndicator {

	private final RediSearchConnectionFactory connectionFactory;

	@Override
	protected void doHealthCheck(Health.Builder builder) throws IOException {
		try (StatefulRedisModulesConnection<String, String> connect = connectionFactory.connect()) {
			if (connect instanceof StatefulRedisModulesClusterConnection<String, String> clusterConnection) {
				String propsFormat = clusterConnection.sync().clusterInfo().replace(":", "=");

				Properties props = new Properties();
				props.load(new StringReader(propsFormat));
				builder.withDetail("cluster_size", Integer.parseInt(props.getProperty("cluster_size")));
				builder.withDetail("slots_ok", Integer.parseInt(props.getProperty("cluster_slots_ok")));
				builder.withDetail("slots_fail", Integer.parseInt(props.getProperty("cluster_slots_fail")));

				if ("fail".equalsIgnoreCase(props.getProperty("cluster_state"))) {
					builder.down();
				}
				else {
					builder.up();
				}
			}
			else {
				String server = connect.sync().info("server");
				Properties props = new Properties();
				props.load(new StringReader(server.replace(":", "=")));
				builder.withDetail("version", props.getProperty("redis_version"));
				builder.up();
			}
		}
	}

}

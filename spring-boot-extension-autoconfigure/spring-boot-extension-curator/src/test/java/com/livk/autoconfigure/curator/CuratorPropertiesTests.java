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

package com.livk.autoconfigure.curator;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class CuratorPropertiesTests {

	@Test
	void curatorProperties() {
		CuratorProperties properties = new CuratorProperties();
		assertThat(properties).isNotNull();

		assertThat(properties.getConnectString()).isEqualTo("localhost:2181");
		assertThat(properties.getBaseSleepTimeMs()).isEqualTo(50);
		assertThat(properties.getMaxRetries()).isEqualTo(10);
		assertThat(properties.getMaxSleepMs()).isEqualTo(500);
		assertThat(properties.getBlockUntilConnectedWait()).isEqualTo(10);
		assertThat(properties.getBlockUntilConnectedUnit()).isEqualTo(TimeUnit.SECONDS);
		assertThat(properties.getSessionTimeout()).isEqualTo(Duration.of(60 * 1000, ChronoUnit.MILLIS));
		assertThat(properties.getConnectionTimeout()).isEqualTo(Duration.of(15 * 1000, ChronoUnit.MILLIS));
	}

}

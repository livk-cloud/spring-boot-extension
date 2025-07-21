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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class CuratorPropertiesTest {

	@Test
	void curatorProperties() {
		CuratorProperties properties = new CuratorProperties();
		assertNotNull(properties);

		assertEquals("localhost:2181", properties.getConnectString());
		assertEquals(50, properties.getBaseSleepTimeMs());
		assertEquals(10, properties.getMaxRetries());
		assertEquals(500, properties.getMaxSleepMs());
		assertEquals(10, properties.getBlockUntilConnectedWait());
		assertEquals(TimeUnit.SECONDS, properties.getBlockUntilConnectedUnit());
		assertEquals(Duration.of(60 * 1000, ChronoUnit.MILLIS), properties.getSessionTimeout());
		assertEquals(Duration.of(15 * 1000, ChronoUnit.MILLIS), properties.getConnectionTimeout());
	}

}

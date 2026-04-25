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

package com.livk.context.mybatis.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class MonitorSQLTimeOutEventTests {

	@Test
	void getSourceReturnsMonitorSQLInfo() {
		MonitorSQLInfo info = new MonitorSQLInfo("select 1", 100L);
		MonitorSQLTimeOutEvent event = new MonitorSQLTimeOutEvent(info);
		assertThat(event.getSource()).isSameAs(info);
		assertThat(event.getSource().sql()).isEqualTo("select 1");
		assertThat(event.getSource().timeout()).isEqualTo(100L);
	}

	@Test
	void isApplicationEvent() {
		MonitorSQLInfo info = new MonitorSQLInfo("select 1", 100L);
		MonitorSQLTimeOutEvent event = new MonitorSQLTimeOutEvent(info);
		assertThat(event).isInstanceOf(org.springframework.context.ApplicationEvent.class);
	}

}

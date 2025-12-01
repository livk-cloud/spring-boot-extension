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

import org.springframework.context.ApplicationEvent;

/**
 * The type Monitor sql time out event.
 *
 * @author livk
 */
public class MonitorSQLTimeOutEvent extends ApplicationEvent {

	/**
	 * Instantiates a new Monitor sql time out event.
	 * @param source the source
	 */
	public MonitorSQLTimeOutEvent(MonitorSQLInfo source) {
		super(source);
	}

	@Override
	public MonitorSQLInfo getSource() {
		return (MonitorSQLInfo) super.getSource();
	}

}

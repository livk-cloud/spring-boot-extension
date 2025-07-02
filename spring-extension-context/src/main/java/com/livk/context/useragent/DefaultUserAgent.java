/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.useragent;

import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * DefaultUserAgent
 * </p>
 *
 * @author livk
 */
record DefaultUserAgent(String userAgentStr, String browser, String browserType, String browserVersion, String os,
		String osVersion, String deviceType, String deviceName, String deviceBrand) implements UserAgent {

	/**
	 * The type User agent builder.
	 */
	@Setter
	@Accessors(chain = true, fluent = true)
	public static class UserAgentBuilder implements Builder {

		private final String userAgentStr;

		private String browser;

		private String browserType;

		private String browserVersion;

		private String os;

		private String osVersion;

		private String deviceType;

		private String deviceName;

		private String deviceBrand;

		/**
		 * Instantiates a new User agent builder.
		 * @param userAgentStr the user agent str
		 */
		UserAgentBuilder(String userAgentStr) {
			this.userAgentStr = userAgentStr;
		}

		/**
		 * Build user agent.
		 * @return the user agent
		 */
		@Override
		public UserAgent build() {
			return new DefaultUserAgent(userAgentStr, browser, browserType, browserVersion, os, osVersion, deviceType,
					deviceName, deviceBrand);
		}

	}
}

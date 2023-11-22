/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.useragent.domain;

/**
 * The type User agent.
 *
 * @author livk
 */
public record UserAgent(String userAgentStr, String browser, String browserType, String browserVersion, String os,
		String osVersion, String deviceType, String deviceName, String deviceBrand) {

	/**
	 * Builder user agent builder.
	 * @param userAgentStr the user agent str
	 * @return the user agent builder
	 */
	public static UserAgentBuilder builder(String userAgentStr) {
		return new UserAgentBuilder(userAgentStr);
	}

	/**
	 * The type User agent builder.
	 */
	public static class UserAgentBuilder {

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
		 * Browser user agent builder.
		 * @param browser the browser
		 * @return the user agent builder
		 */
		public UserAgentBuilder browser(String browser) {
			this.browser = browser;
			return this;
		}

		/**
		 * Browser type user agent builder.
		 * @param browserType the browser type
		 * @return the user agent builder
		 */
		public UserAgentBuilder browserType(String browserType) {
			this.browserType = browserType;
			return this;
		}

		/**
		 * Browser version user agent builder.
		 * @param browserVersion the browser version
		 * @return the user agent builder
		 */
		public UserAgentBuilder browserVersion(String browserVersion) {
			this.browserVersion = browserVersion;
			return this;
		}

		/**
		 * Os user agent builder.
		 * @param os the os
		 * @return the user agent builder
		 */
		public UserAgentBuilder os(String os) {
			this.os = os;
			return this;
		}

		/**
		 * Os version user agent builder.
		 * @param osVersion the os version
		 * @return the user agent builder
		 */
		public UserAgentBuilder osVersion(String osVersion) {
			this.osVersion = osVersion;
			return this;
		}

		/**
		 * Device type user agent builder.
		 * @param deviceType the device type
		 * @return the user agent builder
		 */
		public UserAgentBuilder deviceType(String deviceType) {
			this.deviceType = deviceType;
			return this;
		}

		/**
		 * Device name user agent builder.
		 * @param deviceName the device name
		 * @return the user agent builder
		 */
		public UserAgentBuilder deviceName(String deviceName) {
			this.deviceName = deviceName;
			return this;
		}

		/**
		 * Device brand user agent builder.
		 * @param deviceName the device name
		 * @return the user agent builder
		 */
		public UserAgentBuilder deviceBrand(String deviceName) {
			this.deviceBrand = deviceBrand;
			return this;
		}

		/**
		 * Build user agent.
		 * @return the user agent
		 */
		public UserAgent build() {
			return new UserAgent(userAgentStr, browser, browserType, browserVersion, os, osVersion, deviceType,
					deviceName, deviceBrand);
		}

	}
}

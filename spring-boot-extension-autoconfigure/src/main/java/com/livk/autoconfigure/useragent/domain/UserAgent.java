/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.useragent.domain;

/**
 * @author livk
 */
public record UserAgent(String userAgentStr,
						String browser,
						String browserType,
						String browserVersion,
						String os,
						String osVersion,
						String deviceType,
						String deviceName,
						String deviceBrand) {
	public static UserAgentBuilder builder(String userAgentStr) {
		return new UserAgentBuilder(userAgentStr);
	}

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

		UserAgentBuilder(String userAgentStr) {
			this.userAgentStr = userAgentStr;
		}

		public UserAgentBuilder browser(String browser) {
			this.browser = browser;
			return this;
		}

		public UserAgentBuilder browserType(String browserType) {
			this.browserType = browserType;
			return this;
		}

		public UserAgentBuilder browserVersion(String browserVersion) {
			this.browserVersion = browserVersion;
			return this;
		}

		public UserAgentBuilder os(String os) {
			this.os = os;
			return this;
		}

		public UserAgentBuilder osVersion(String osVersion) {
			this.osVersion = osVersion;
			return this;
		}

		public UserAgentBuilder deviceType(String deviceType) {
			this.deviceType = deviceType;
			return this;
		}

		public UserAgentBuilder deviceName(String deviceName) {
			this.deviceName = deviceName;
			return this;
		}

		public UserAgentBuilder deviceBrand(String deviceName) {
			this.deviceBrand = deviceBrand;
			return this;
		}

		public UserAgent build() {
			return new UserAgent(userAgentStr, browser, browserType, browserVersion,
				os, osVersion, deviceType, deviceName, deviceBrand);
		}
	}
}

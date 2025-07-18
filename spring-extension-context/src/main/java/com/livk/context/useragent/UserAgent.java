/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
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

package com.livk.context.useragent;

/**
 * @author livk
 */
public interface UserAgent {

	static Builder builder(String userAgentStr) {
		return new DefaultUserAgent.UserAgentBuilder(userAgentStr);
	}

	String userAgentStr();

	String browser();

	String browserType();

	String browserVersion();

	String os();

	String osVersion();

	String deviceType();

	String deviceName();

	String deviceBrand();

	interface Builder {

		Builder browser(String browser);

		/**
		 * Browser type user agent builder.
		 * @param browserType the browser type
		 * @return the user agent builder
		 */
		Builder browserType(String browserType);

		/**
		 * Browser version user agent builder.
		 * @param browserVersion the browser version
		 * @return the user agent builder
		 */
		Builder browserVersion(String browserVersion);

		/**
		 * Os user agent builder.
		 * @param os the os
		 * @return the user agent builder
		 */
		Builder os(String os);

		/**
		 * Os version user agent builder.
		 * @param osVersion the os version
		 * @return the user agent builder
		 */
		Builder osVersion(String osVersion);

		/**
		 * Device type user agent builder.
		 * @param deviceType the device type
		 * @return the user agent builder
		 */
		Builder deviceType(String deviceType);

		/**
		 * Device name user agent builder.
		 * @param deviceName the device name
		 * @return the user agent builder
		 */
		Builder deviceName(String deviceName);

		/**
		 * Device brand user agent builder.
		 * @param deviceBrand the device name
		 * @return the user agent builder
		 */
		Builder deviceBrand(String deviceBrand);

		/**
		 * Build user agent.
		 * @return the user agent
		 */
		UserAgent build();

	}

}

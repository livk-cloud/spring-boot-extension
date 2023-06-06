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

package com.livk.autoconfigure.useragent.servlet;

import com.livk.commons.bean.Wrapper;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

/**
 * The type User agent context holder.
 *
 * @author livk
 */
public class UserAgentContextHolder {

	private static final ThreadLocal<Wrapper> context = new NamedThreadLocal<>("useragent context");

	private static final ThreadLocal<Wrapper> inheritableContext = new NamedInheritableThreadLocal<>("inheritable useragent context");

	/**
	 * Sets user agent context.
	 *
	 * @param useragentWrapper the useragent wrapper
	 * @param inheritable      the inheritable
	 */
	public static void setUserAgentContext(Wrapper useragentWrapper, boolean inheritable) {
		if (useragentWrapper != null) {
			if (inheritable) {
				inheritableContext.set(useragentWrapper);
				context.remove();
			} else {
				context.set(useragentWrapper);
				inheritableContext.remove();
			}
		} else {
			cleanUserAgentContext();
		}
	}

	/**
	 * Gets user agent context.
	 *
	 * @return the user agent context
	 */
	public static Wrapper getUserAgentContext() {
		Wrapper useragentWrapper = context.get();
		if (useragentWrapper == null) {
			useragentWrapper = inheritableContext.get();
		}
		return useragentWrapper;
	}

	/**
	 * Sets user agent context.
	 *
	 * @param useragentWrapper the useragent wrapper
	 */
	public static void setUserAgentContext(Wrapper useragentWrapper) {
		setUserAgentContext(useragentWrapper, false);
	}

	/**
	 * Clean user agent context.
	 */
	public static void cleanUserAgentContext() {
		context.remove();
		inheritableContext.remove();
	}
}

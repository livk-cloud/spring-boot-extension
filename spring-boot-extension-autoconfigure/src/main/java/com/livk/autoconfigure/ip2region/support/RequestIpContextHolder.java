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

package com.livk.autoconfigure.ip2region.support;

import com.livk.autoconfigure.ip2region.doamin.IpInfo;
import org.springframework.core.NamedInheritableThreadLocal;

import java.util.function.Supplier;

/**
 * <p>
 * RequestIpContextHolder
 * </p>
 *
 * @author livk
 */
public class RequestIpContextHolder {
	private static final ThreadLocal<IpInfo> inheritableContext = new NamedInheritableThreadLocal<>("inheritable request ip context");

	/**
	 * Set.
	 *
	 * @param ipInfo the ip
	 */
	public static void set(IpInfo ipInfo) {
		if (ipInfo != null) {
			inheritableContext.set(ipInfo);
		} else {
			remove();
		}
	}

	/**
	 * Compute if absent string.
	 *
	 * @param supplier the supplier
	 * @return the string
	 */
	public synchronized static IpInfo computeIfAbsent(Supplier<IpInfo> supplier) {
		IpInfo ipInfo = get();
		if (ipInfo != null) {
			return ipInfo;
		}
		IpInfo supplierIp = supplier.get();
		set(supplierIp);
		return supplierIp;
	}

	/**
	 * Get string.
	 *
	 * @return the string
	 */
	public static IpInfo get() {
		return inheritableContext.get();
	}

	/**
	 * Remove.
	 */
	public static void remove() {
		inheritableContext.remove();
	}
}

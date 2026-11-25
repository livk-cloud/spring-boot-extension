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

package com.livk.context.sequence.support;

import java.net.InetAddress;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * Derives snowflake worker coordinates when they are not configured explicitly.
 *
 * @author livk
 */
public final class SnowflakeWorkerIds {

	/**
	 * Snowflake machine-id bit width supports values {@code 0-31}.
	 */
	static final int MACHINE_ID_MOD = 32;

	private SnowflakeWorkerIds() {
	}

	/**
	 * Build a machine id from host name and process id.
	 * @return a value in {@code 0-31}
	 */
	public static long machineId() {
		return machineId(SnowflakeWorkerIds::hostName, SnowflakeWorkerIds::processId);
	}

	/**
	 * Build a machine id from the given host and process suppliers.
	 * @param hostName host name supplier
	 * @param processId process id supplier
	 * @return a value in {@code 0-31}
	 */
	static long machineId(Supplier<String> hostName, LongSupplier processId) {
		String seed = hostName.get() + ':' + processId.getAsLong();
		return Math.floorMod(seed.hashCode(), MACHINE_ID_MOD);
	}

	static String hostName() {
		return hostName(() -> InetAddress.getLocalHost().getHostName());
	}

	static String hostName(HostNameResolver resolver) {
		try {
			return resolver.resolve();
		}
		catch (Exception ex) {
			return "localhost";
		}
	}

	static long processId() {
		return processId(() -> ProcessHandle.current().pid());
	}

	static long processId(LongSupplier resolver) {
		try {
			return resolver.getAsLong();
		}
		catch (Exception ex) {
			return 0L;
		}
	}

	@FunctionalInterface
	interface HostNameResolver {

		String resolve() throws Exception;

	}

}

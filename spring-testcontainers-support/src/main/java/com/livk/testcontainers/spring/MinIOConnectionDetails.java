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

package com.livk.testcontainers.spring;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * @author livk
 */
public interface MinIOConnectionDetails extends ConnectionDetails {

	default String getEndpoint() {
		return null;
	}

	default String getUserName() {
		return null;
	}

	default String getPassword() {
		return null;
	}

}

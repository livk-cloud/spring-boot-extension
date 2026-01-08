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

package com.livk.context.limit.exception;

import com.livk.context.limit.annotation.Limit;

/**
 * @author livk
 */
public interface LimitExceededHandler {

	RuntimeException buildException(Limit limit);

	LimitExceededHandler DEFAULT = limit -> new LimitException("Limit exceeded: key=%s, max=%d, interval=%d %s"
		.formatted(limit.key(), limit.rate(), limit.rateInterval(), limit.rateIntervalUnit()));

}

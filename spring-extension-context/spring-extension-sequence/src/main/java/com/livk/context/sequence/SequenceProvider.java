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

package com.livk.context.sequence;

import org.springframework.core.Ordered;

/**
 * Supplies a {@link Sequence} for a business name.
 * <p>
 * Range-based implementations keep an independent number range per name. Snowflake
 * implementations may share one generator across names.
 *
 * @author livk
 */
public interface SequenceProvider extends Ordered {

	/**
	 * Return the sequence for the given business name.
	 * @param bizName the business name
	 * @return the sequence
	 */
	Sequence get(String bizName);

	@Override
	default int getOrder() {
		return 0;
	}

}

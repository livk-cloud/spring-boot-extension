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

import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.exception.SequenceException;
import org.springframework.util.StringUtils;

/**
 * @author livk
 */
public abstract class AbstractRangeManager implements RangeManager {

	@Override
	public final SequenceRange nextRange(String name, int step, long stepStart) {
		if (!StringUtils.hasText(name)) {
			throw new SequenceException("[RangeManager-nextRange] name is empty.");
		}
		try {
			return buildNextRange(name, step, stepStart);
		}
		catch (SequenceException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new SequenceException("[RangeManager-nextRange] buildNextRange error.", ex);
		}
	}

	protected abstract SequenceRange buildNextRange(String name, int step, long stepStart);

}

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

package com.livk.batch.support;

import com.livk.batch.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.jspecify.annotations.Nullable;
import org.springframework.util.Assert;

/**
 * @author livk
 */
@Slf4j
public class JobWriteListener implements ItemWriteListener<User> {

	@Override
	public void beforeWrite(@Nullable Chunk<? extends User> items) {
		log.info("spring batch start write,data:{}", items);
	}

	@Override
	public void afterWrite(@Nullable Chunk<? extends User> items) {
		log.info("spring batch is write,data:{}", items);
	}

	@Override
	public void onWriteError(@Nullable Exception e, @Nullable Chunk<? extends User> items) {
		Assert.notNull(e, "exception must not be null");
		log.error("spring batch write an error occurred ,message:{} data:{}", e.getMessage(), items, e);
	}

}

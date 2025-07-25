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
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.time.LocalDateTime;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class CsvItemProcessor implements ItemProcessor<User, User> {

	private final Validator<? super User> validator;

	@Override
	public User process(@NonNull User item) throws ValidationException {
		try {
			validator.validate(item);
			if (item.getSex().equals("男")) {
				item.setSex("1");
			}
			else {
				item.setSex("2");
			}
			item.setStatus(1);
			item.setCreateTime(LocalDateTime.now());
			item.setUpdateTime(LocalDateTime.now());
			return item;
		}
		catch (ValidationException e) {
			return null;
		}
	}

}

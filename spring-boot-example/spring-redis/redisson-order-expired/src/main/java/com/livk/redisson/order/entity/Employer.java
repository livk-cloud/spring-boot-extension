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

package com.livk.redisson.order.entity;

import com.livk.commons.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * Employer
 * </p>
 *
 * @author livk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employer {
	private String name;
	private int age;
	private String wife;
	private Double salary;
	private String putTime;

	public void setPutTime() {
		this.putTime = DateUtils.format(LocalDateTime.now(), DateUtils.HMS);
	}
}

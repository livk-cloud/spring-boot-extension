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

package com.livk.sso.commons.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author livk
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role implements GrantedAuthority {

	private Integer id;

	private String roleName;

	private String roleDescription;

	@JsonCreator
	public static Role create(@JsonProperty("authority") String authority) {
		Role role = new Role();
		role.setRoleName(authority);
		return role;
	}

	@JsonIgnore
	@Override
	public String getAuthority() {
		return roleName;
	}

}

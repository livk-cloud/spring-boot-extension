/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.filter.context;

import com.livk.commons.util.ObjectUtils;
import lombok.experimental.UtilityClass;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

/**
 * <p>
 * TenantContextHolder
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class TenantContextHolder {

	public final static String ATTRIBUTES = "tenant";

	private final static ThreadLocal<String> TENANT_ID = new NamedThreadLocal<>("tenant context");

	private final static ThreadLocal<String> INHERITABLE_TENANT_ID = new NamedInheritableThreadLocal<>(
			"inheritable tenant ip context");

	public String getTenantId() {
		String tenantId = TENANT_ID.get();
		if (ObjectUtils.isEmpty(tenantId)) {
			tenantId = INHERITABLE_TENANT_ID.get();
		}
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		setTenantId(tenantId, false);
	}

	public void setTenantId(String tenantId, boolean inheritable) {
		if (StringUtils.hasText(tenantId)) {
			if (inheritable) {
				INHERITABLE_TENANT_ID.set(tenantId);
				TENANT_ID.remove();
			}
			else {
				TENANT_ID.set(tenantId);
				INHERITABLE_TENANT_ID.remove();
			}
		}
		else {
			remove();
		}
	}

	public void remove() {
		TENANT_ID.remove();
		INHERITABLE_TENANT_ID.remove();
	}

}

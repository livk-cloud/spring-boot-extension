package com.livk.filter.context;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

/**
 * <p>
 * TenantContext
 * </p>
 *
 * @author livk
 * @date 2022/5/10
 */
@UtilityClass
public class TenantContext {

	public final static String ATTRIBUTES = "tenant";

	private final static ThreadLocal<String> TENANT_ID = new ThreadLocal<>();

	public String getTenantId() {
		return TENANT_ID.get();
	}

	public synchronized void setTenantId(String tenantId) {
		if (StringUtils.hasText(tenantId)) {
			TENANT_ID.set(tenantId);
		}
	}

	public void remove() {
		TENANT_ID.remove();
	}

}

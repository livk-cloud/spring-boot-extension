package com.livk.filter.context;

import lombok.experimental.UtilityClass;
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

    private final static ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private final static ThreadLocal<String> INHERITABLE_TENANT_ID = new InheritableThreadLocal<>();

    public String getTenantId() {
        String tenantId = TENANT_ID.get();
        if (!StringUtils.hasText(tenantId)) {
            tenantId = INHERITABLE_TENANT_ID.get();
        }
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        if (StringUtils.hasText(tenantId)) {
            TENANT_ID.set(tenantId);
            INHERITABLE_TENANT_ID.set(tenantId);
        }
    }

    public void remove() {
        TENANT_ID.remove();
        INHERITABLE_TENANT_ID.remove();
    }

}

package com.livk.filter.context;

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
    private final static ThreadLocal<String> INHERITABLE_TENANT_ID = new NamedInheritableThreadLocal<>("inheritable tenant ip context");


    public String getTenantId() {
        String tenantId = TENANT_ID.get();
        if (!StringUtils.hasText(tenantId)) {
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
            } else {
                TENANT_ID.set(tenantId);
                INHERITABLE_TENANT_ID.remove();
            }
        } else {
            remove();
        }
    }

    public void remove() {
        TENANT_ID.remove();
        INHERITABLE_TENANT_ID.remove();
    }

}

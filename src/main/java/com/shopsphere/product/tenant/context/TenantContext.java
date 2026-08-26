package com.shopsphere.product.tenant.context;

import com.shopsphere.product.exception.MissingTenantContextException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(String id) {
        CURRENT_TENANT.set(id);
    }

    private static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static String requireTenantId() {
        String tenantId = getCurrentTenant();

        if (!StringUtils.hasText(tenantId)) {
            throw new MissingTenantContextException("Tenant context is missing.");
        }

        return tenantId;
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}

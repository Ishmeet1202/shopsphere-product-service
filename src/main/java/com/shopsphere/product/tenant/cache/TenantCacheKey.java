package com.shopsphere.product.tenant.cache;

import com.shopsphere.product.tenant.context.TenantContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TenantCacheKey {

    public static String product(String productId) {
        String tenantId = TenantContext.getCurrentTenant();
        return tenantId + ":" + productId;
    }
}

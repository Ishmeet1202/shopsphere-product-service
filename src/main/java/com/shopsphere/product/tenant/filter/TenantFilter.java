package com.shopsphere.product.tenant.filter;

import com.shopsphere.product.exception.MissingTenantContextException;
import com.shopsphere.product.tenant.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantFilter.class);
    private static final String TENANT_HEADER = "X-Tenant-Id";
    private final HandlerExceptionResolver exceptionResolver;

    public TenantFilter(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver
    ) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String tenantId = request.getHeader(TENANT_HEADER);

            if (!StringUtils.hasText(tenantId)) {
                LOGGER.error("Missing tenant ID in request header: {}", TENANT_HEADER);
                throw new MissingTenantContextException("Tenant ID is missing in the request header: " + TENANT_HEADER);
            }

            TenantContext.setCurrentTenant(tenantId);
            LOGGER.info("Set tenant ID in context: {}", tenantId);

            filterChain.doFilter(request, response);
        } catch (MissingTenantContextException ex) {
            exceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    ex
            );
        } finally {
            TenantContext.clear();
        }
    }
}

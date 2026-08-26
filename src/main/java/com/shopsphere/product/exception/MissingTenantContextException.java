package com.shopsphere.product.exception;

public class MissingTenantContextException extends RuntimeException {
    public MissingTenantContextException(String message) {
        super(message);
    }
}

package com.forgerock.openbanking.model.error;

public class UnsupportedOIDCAuthMethodsException extends RuntimeException {
    public UnsupportedOIDCAuthMethodsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOIDCAuthMethodsException(String message) {
        super(message);
    }
}

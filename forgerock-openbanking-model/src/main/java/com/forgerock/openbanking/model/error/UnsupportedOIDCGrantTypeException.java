package com.forgerock.openbanking.model.error;

public class UnsupportedOIDCGrantTypeException extends RuntimeException {
    public UnsupportedOIDCGrantTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOIDCGrantTypeException(String message) {
        super(message);
    }
}

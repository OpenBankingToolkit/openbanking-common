/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.ssl.exceptions;

public class RestTemplateLoadingException extends Exception {
    public RestTemplateLoadingException() {
    }

    public RestTemplateLoadingException(String message) {
        super(message);
    }

    public RestTemplateLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestTemplateLoadingException(Throwable cause) {
        super(cause);
    }

    public RestTemplateLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

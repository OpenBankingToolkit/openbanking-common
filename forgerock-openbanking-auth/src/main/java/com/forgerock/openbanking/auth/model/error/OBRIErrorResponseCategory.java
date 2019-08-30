/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.error;

public enum OBRIErrorResponseCategory {
    REQUEST_FILTER("OBRI.Request.Filter", "An error happened when filtering your request before accessing the resource"),
    ARGUMENT_INVALID("OBRI.Argument.Invalid", "An error happened when parsing the request arguments"),
    REQUEST_INVALID("OBRI.Request.Invalid", "An error happened when parsing the request arguments"),
    SERVER_INTERNAL_ERROR("OBRI.Server.InternalError", "An error happened on the server side"),
    TPP_REGISTRATION("OBRI.TPP.Registration", "An error happened when using the TPP registration endpoint"),
    HEADLESS_AUTH("OBRI.HeadLess.Auth", "An error happened when doing headless auth"),
    ACCESS_TOKEN("OBRI.AccessToken", "An error happened when using the AS access token endpoint"),
    MANUAL_ONBOARDING("OBRI.ManualOnboarding", "An error happened on the manual onboarding"),
    ;

    OBRIErrorResponseCategory(String id, String description) {
        this.id = id;
        this.description = description;
    }

    private String id;
    private String description;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}

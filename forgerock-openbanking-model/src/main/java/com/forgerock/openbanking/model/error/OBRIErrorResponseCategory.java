/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.model.error;

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

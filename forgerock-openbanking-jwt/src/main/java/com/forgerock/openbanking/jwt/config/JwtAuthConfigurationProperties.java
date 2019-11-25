/**
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
package com.forgerock.openbanking.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "jwt-auth")
public class JwtAuthConfigurationProperties {

    private String expectedIssuerId;
    private String expectedAudienceId;
    private String jwkUri;
    private String obUserInfo;
    private String authUserInfo;
    private boolean cookie = false;

    public String getExpectedIssuerId() {
        return expectedIssuerId;
    }

    public void setExpectedIssuerId(String expectedIssuerId) {
        this.expectedIssuerId = expectedIssuerId;
    }

    public String getExpectedAudienceId() {
        return expectedAudienceId;
    }

    public void setExpectedAudienceId(String expectedAudienceId) {
        this.expectedAudienceId = expectedAudienceId;
    }

    public String getJwkUri() {
        return jwkUri;
    }

    public void setJwkUri(String jwkUri) {
        this.jwkUri = jwkUri;
    }

    public String getObUserInfo() {
        return obUserInfo;
    }

    public void setObUserInfo(String obUserInfo) {
        this.obUserInfo = obUserInfo;
    }

    public String getAuthUserInfo() {
        return authUserInfo;
    }

    public void setAuthUserInfo(String authUserInfo) {
        this.authUserInfo = authUserInfo;
    }

    public boolean isCookie() {
        return cookie;
    }

    public void setCookie(boolean cookie) {
        this.cookie = cookie;
    }
}

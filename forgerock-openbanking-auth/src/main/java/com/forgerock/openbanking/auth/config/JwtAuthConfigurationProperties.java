/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.config;

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

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
package com.forgerock.openbanking.model.oidc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.Data;

import java.text.ParseException;

/**
 * Models a response from the OAuth 2.0 token endpoint. See RFC 6749 § 5.1.
 */
@Data
public class AccessTokenResponse {
    /**
     * The access token issued by the authorization server.
     */
    public String access_token;

    /**
     * The lifetime of the access token.
     */
    public long expires_in;

    /**
     * The openId token issued by the authorization server.
     */
    public String id_token;

    public String token_type;

    public String scope;

    public String refresh_token;

    public AccessTokenResponse() {
    }

    /**
     * Constructs a new response.
     *
     * @param accessToken The access token issued by the authorization server.
     * @param expiresIn   The lifetime of the access token.
     * @param openIdToken The openId token issued by the authorization server.
     * @param token_type
     * @param scope
     */
    public AccessTokenResponse(String accessToken, long expiresIn,
                               String openIdToken, String token_type, String scope, String refresh_token) {
        this.access_token = accessToken;
        this.expires_in = expiresIn;
        this.id_token = openIdToken;
        this.token_type = token_type;
        this.scope = scope;
        this.refresh_token = refresh_token;
    }

    @JsonIgnore
    public SignedJWT getAccessTokenJWT() throws ParseException {
        return (SignedJWT) JWTParser.parse(access_token);
    }

    @JsonIgnore
    public String getIdToken() {
        return id_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}

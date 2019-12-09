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
package com.forgerock.openbanking.am.services;

import com.forgerock.openbanking.am.gateway.AMASPSPGateway;
import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.am.gateway.AMMatlsASPSPGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@Slf4j
public class AMGatewayService {

    @Autowired
    private AMASPSPGateway amGateway;
    @Autowired
    private AMMatlsASPSPGateway amMatlsGateway;
    @Value("${am.matls-hostname}")
    private String amMatlsHostname;

    public AMGateway getAmGateway(String jwt) throws OBErrorResponseException {
        String tokenEndpoint = "https://" + amMatlsHostname + "/oauth2/access_token";
        log.debug("Verify the audience from the jwt {} is equal to the token endpoint {}", jwt, tokenEndpoint);
        try {
            SignedJWT jws = (SignedJWT) JWTParser.parse(jwt);
            if (jws.getJWTClaimsSet().getAudience() != null && jws.getJWTClaimsSet().getAudience().contains(tokenEndpoint)) {
                log.debug("Found the token endpoint as audience => Use the MATLS gateway");
                return this.amMatlsGateway;
            }
        } catch (ParseException e) {
            log.error("Parse JWT error", e);
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID.toOBError1(jwt));
        }
        log.debug("The token endpoint wasnt used as audience => Use the non-MATLS gateway");
        return this.amGateway;
    }
}

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

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
@Slf4j
public class AMResourceServerService {

    @Autowired
    private CryptoApiClient cryptoApiClient;
    @Autowired
    private AMOpenBankingConfiguration amOpenBankingConfiguration;

    /**
     * Verify a stateless access token
     * @param accessTokenBearer an access token bearer or the JWT directly
     * @return the JWS
     * @throws ParseException can't parse the access token JWT, must not be a stateless JWT
     * @throws InvalidTokenException the access token is invalid
     */
    public SignedJWT verifyAccessToken(String accessTokenBearer)
            throws ParseException, InvalidTokenException, IOException {
        accessTokenBearer = accessTokenBearer.replaceFirst("^Bearer ", "");
        SignedJWT signedAccessToken = (SignedJWT) JWTParser.parse(accessTokenBearer);
        cryptoApiClient.validateJws(accessTokenBearer,null, amOpenBankingConfiguration.jwksUri);
        if (!amOpenBankingConfiguration.audiences.contains(signedAccessToken.getJWTClaimsSet().getIssuer())) {
            log.debug("Invalid audience {}, expecting {}", signedAccessToken.getJWTClaimsSet().getIssuer(), amOpenBankingConfiguration.audiences);
            throw new InvalidTokenException("Invalid audience '" + signedAccessToken.getJWTClaimsSet().getIssuer() + "', expecting '" +
                    amOpenBankingConfiguration.audiences + "'");
        }
        return signedAccessToken;
    }
}

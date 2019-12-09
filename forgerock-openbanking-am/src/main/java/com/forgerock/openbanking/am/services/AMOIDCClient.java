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
import com.forgerock.openbanking.am.gateway.AMASPSPGateway;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AMOIDCClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(AMOIDCRegistrationService.class);

    @Autowired
    private AMASPSPGateway amGateway;
    @Autowired
    private AMOpenBankingConfiguration amOpenBankingConfiguration;

    public void deleteOIDCClient(String oidcClientId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "iPlanetDirectoryPro=" + token);
        headers.add("Accept-API-Version", "protocol=1.0,resource=1.0");
        //Send request
        HttpEntity<OIDCRegistrationRequest> request = new HttpEntity<>(headers);

        LOGGER.debug("Delete OIDC client {}", oidcClientId);

        amGateway.toAM(amOpenBankingConfiguration.oidcClient + "/" + oidcClientId, HttpMethod.DELETE, headers,
                new ParameterizedTypeReference<AccessTokenResponse>() {}, null);
    }

    public List<String> allOIDCClients(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "iPlanetDirectoryPro=" + token);
        headers.add("Accept-API-Version", "protocol=1.0,resource=1.0");
        //Send request
        HttpEntity<OIDCRegistrationRequest> request = new HttpEntity<>(headers);

        LOGGER.debug("Read all OIDC clients");

        OIDCResponse oidcResponse = (OIDCResponse) amGateway.toAM(amOpenBankingConfiguration.oidcClient + "?_queryFilter=true" , HttpMethod.GET, headers,
                new ParameterizedTypeReference<OIDCResponse>() {}, null).getBody();
        return oidcResponse.getResult().stream().map(OIDCResponse.OIDCClient::get_id).collect(Collectors.toList());
    }

    private static class OIDCResponse {
        List<OIDCClient> result;

        public List<OIDCClient> getResult() {
            return result;
        }

        public void setResult(List<OIDCClient> result) {
            this.result = result;
        }

        private static class OIDCClient {
            private String _id;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }
        }
    }

}

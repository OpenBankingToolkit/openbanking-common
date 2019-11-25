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
package com.forgerock.openbanking.am.services;

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.gateway.AMASPSPGateway;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class AMOIDCRegistrationService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AMOIDCRegistrationService.class);

    @Autowired
    private AMASPSPGateway amGateway;
    @Autowired
    private AMOpenBankingConfiguration amOpenBankingConfiguration;

    public OIDCRegistrationResponse register(OIDCRegistrationRequest oidcRegistrationRequest) {
        LOGGER.info("Register a new OIDC client to AM");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Send request
        LOGGER.debug("Send dynamic registration POST request to the AS {} with headers {}", amOpenBankingConfiguration.registrationEndpoint, headers);

        return (OIDCRegistrationResponse) amGateway.toAM(amOpenBankingConfiguration.registrationEndpoint, HttpMethod.POST, headers, new ParameterizedTypeReference<OIDCRegistrationResponse>(){}, oidcRegistrationRequest)
                .getBody();
    }

    public OIDCRegistrationResponse updateOIDCClient(String token, OIDCRegistrationRequest oidcRegistrationRequest, String clientId) {
        LOGGER.info("Update OIDC client to AM");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        //Send request
        LOGGER.debug("Send dynamic registration PUT request to the AS {} with headers {}", amOpenBankingConfiguration.registrationEndpoint  + "?client_id=" + clientId, headers);

        return (OIDCRegistrationResponse) amGateway.toAM(amOpenBankingConfiguration.registrationEndpoint  + "?client_id=" + clientId, HttpMethod.PUT, headers, new ParameterizedTypeReference<OIDCRegistrationResponse>(){}, oidcRegistrationRequest)
                .getBody();
    }

    public OIDCRegistrationResponse deleteOIDCClient(String token, String clientId) {
        LOGGER.info("Delete OIDC client to AM");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        //Send request
        LOGGER.debug("Send dynamic registration DELETE request to the AS {} with headers {}", amOpenBankingConfiguration.registrationEndpoint  + "?client_id=" + clientId, headers);

        return (OIDCRegistrationResponse) amGateway.toAM(amOpenBankingConfiguration.registrationEndpoint  + "?client_id=" + clientId, HttpMethod.DELETE, headers, new ParameterizedTypeReference<OIDCRegistrationResponse>(){}, null)
                .getBody();
    }

    public OIDCRegistrationResponse getOIDCClient(String token, String clientId) {
        LOGGER.info("Get OIDC client to AM");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        //Send request
        LOGGER.debug("Send dynamic registration GET request to the AS {} with headers {}", amOpenBankingConfiguration.registrationEndpoint  + "?client_id=" + clientId, headers);

        return (OIDCRegistrationResponse) amGateway.toAM(amOpenBankingConfiguration.registrationEndpoint  + "?client_id=" + clientId, HttpMethod.GET, headers, new ParameterizedTypeReference<OIDCRegistrationResponse>(){}, null)
                .getBody();
    }
}

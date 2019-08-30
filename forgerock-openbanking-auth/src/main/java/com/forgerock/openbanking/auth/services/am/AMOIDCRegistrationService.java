/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.am;

import com.forgerock.openbanking.auth.model.oidc.OIDCRegistrationRequest;
import com.forgerock.openbanking.auth.model.oidc.OIDCRegistrationResponse;
import com.forgerock.openbanking.auth.services.aspsp.AMASPSPGateway;
import com.forgerock.openbanking.core.config.AMOpenBankingConfiguration;
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

/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.am;

import com.forgerock.openbanking.auth.model.oidc.AccessTokenResponse;
import com.forgerock.openbanking.auth.model.oidc.OIDCRegistrationRequest;
import com.forgerock.openbanking.auth.services.aspsp.AMASPSPGateway;
import com.forgerock.openbanking.core.config.AMOpenBankingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

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

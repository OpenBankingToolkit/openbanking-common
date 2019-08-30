/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.am;

import com.forgerock.openbanking.auth.services.aspsp.AMASPSPGateway;
import com.forgerock.openbanking.core.config.AMOpenBankingConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class AMAuthentication {
    private final static Logger LOGGER = LoggerFactory.getLogger(AMOIDCRegistrationService.class);
    @Resource(name = "forExternalForgeRockApplication")
    private RestTemplate restTemplate;
    @Autowired
    private AMOpenBankingConfiguration amOpenBankingConfiguration;
    @Value("${am.internal.root}")
    private String amRoot;
    @Value("${am.matls-hostname}")
    private String amMatlsHostname;
    @Value("${scgw.port}")
    private String scgwPort;
    @Autowired
    private AMASPSPGateway amGateway;

    public TokenResponse authenticateAsAMAdmin() {
        return authenticate(false,"amadmin", amOpenBankingConfiguration.amadminPassword, null);
    }

    public TokenResponse authenticate(String login, String password) {
        return authenticate(login, password, null);
    }

    public TokenResponse authenticate(String login, String password, String service) {
       return authenticate(true, login, password, service);
    }

    public TokenResponse authenticate(boolean sendXForwardingHeader, String login, String password, String service) {
        LOGGER.info("Register a new OIDC client to AM");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-OpenAM-Username", login);
        headers.add("X-OpenAM-Password", password);
        headers.add("Cookie", "iPlanetDirectoryPro=");
        headers.add("Accept-API-Version", "protocol=1.0,resource=1.0");

        String url = amOpenBankingConfiguration.authentication;
        if (service != null) {
            url += "?authIndexType=service&authIndexValue=" + service;
        }
        LOGGER.debug("Authenticate to AM {}, login='{}' and password='{}'", url, login, password);

        return (TokenResponse) amGateway.toAM(url, sendXForwardingHeader, HttpMethod.POST, headers, new ParameterizedTypeReference<TokenResponse>() {}, null).getBody();
    }


    public static class TokenResponse {
        private String tokenId;
        private String successUrl;
        private String realm;

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getSuccessUrl() {
            return successUrl;
        }

        public void setSuccessUrl(String successUrl) {
            this.successUrl = successUrl;
        }

        public String getRealm() {
            return realm;
        }

        public void setRealm(String realm) {
            this.realm = realm;
        }
    }
}

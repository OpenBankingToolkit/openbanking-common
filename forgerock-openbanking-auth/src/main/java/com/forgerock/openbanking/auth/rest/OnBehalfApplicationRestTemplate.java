/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.rest;

import com.forgerock.openbanking.ssl.config.SslConfiguration;
import com.forgerock.openbanking.ssl.exceptions.SslConfigurationFailure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class OnBehalfApplicationRestTemplate extends RestTemplate {
    private boolean checkHostname;

    @Autowired
    private SslConfiguration sslConfiguration;

    public OnBehalfApplicationRestTemplate(ClientHttpRequestFactory requestFactory, boolean checkHostname) {
        super(requestFactory);
        this.checkHostname = checkHostname;
    }

    public void setApplication(String clientCertificateAlias) throws SslConfigurationFailure {
        HttpComponentsClientHttpRequestFactory factory =
                sslConfiguration.factory(clientCertificateAlias, checkHostname);
        setRequestFactory(factory);
    }
}

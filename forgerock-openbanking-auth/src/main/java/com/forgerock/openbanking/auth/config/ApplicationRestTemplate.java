/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.config;

import com.forgerock.openbanking.ssl.config.SslConfiguration;
import com.forgerock.openbanking.ssl.exceptions.RestTemplateLoadingException;
import com.forgerock.openbanking.ssl.exceptions.SslConfigurationFailure;
import com.forgerock.openbanking.ssl.model.ForgeRockApplicationResponse;
import com.forgerock.openbanking.ssl.services.CertificateLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.*;

import java.net.URI;

@Slf4j
public class ApplicationRestTemplate extends RestTemplate {

    private boolean checkHostname;
    private SslConfiguration sslConfiguration;
    private CertificateLoader certificateLoader;


    public ApplicationRestTemplate(CertificateLoader certificateLoader,
                                   SslConfiguration sslConfiguration,
                                   ClientHttpRequestFactory requestFactory,
                                   boolean checkHostname) {
        super(requestFactory);
        this.checkHostname = checkHostname;
        this.certificateLoader = certificateLoader;
        this.sslConfiguration = sslConfiguration;
    }

    public void setApplication(ForgeRockApplicationResponse application) throws SslConfigurationFailure {
        HttpComponentsClientHttpRequestFactory factory =
                sslConfiguration.factory(application.getTransportKey().getKeyID(), checkHostname);
        setRequestFactory(factory);
    }

    protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback,
                              @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {
        log.trace("RestTemplate->doExecute url {} and method {}", url, method);
        try {
            return super.doExecute(url, method, requestCallback, responseExtractor);
        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException) {
               if (((HttpClientErrorException) e).getStatusCode() == HttpStatus.FORBIDDEN ||
                      ((HttpClientErrorException) e).getStatusCode() == HttpStatus.UNAUTHORIZED) {
                   log.warn("Couldn't access the service, received 401 or 403. Let's try to reload our client certificate");
                    try {
                        setApplication(certificateLoader.refreshCurrentApplication());
                        return super.doExecute(url, method, requestCallback, responseExtractor);
                    } catch (RestTemplateLoadingException | SslConfigurationFailure e1) {
                        log.error("Couldn't refresh the application, we throw back the initial exception", e1);
                    }
                }
            }
            throw e;
        }
    }

}

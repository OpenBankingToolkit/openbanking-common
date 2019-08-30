/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.ssl.services;

import com.forgerock.cert.utils.CertificateUtils;
import com.forgerock.openbanking.ssl.exceptions.RestTemplateLoadingException;
import com.forgerock.openbanking.ssl.model.ForgeRockApplicationResponse;
import com.forgerock.openbanking.ssl.services.keystore.KeyStoreService;
import com.nimbusds.jose.jwk.AsymmetricJWK;
import com.nimbusds.jose.jwk.JWK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.cert.Certificate;

@Service
public class CertificateLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateLoader.class);

    @Autowired
    private KeyStoreService keyStoreService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ForgeRockApplicationService forgeRockApplicationService;

    public ForgeRockApplicationResponse refreshCurrentApplication() throws RestTemplateLoadingException {
        try {
            ForgeRockApplicationResponse application = forgeRockApplicationService.getCurrentApplication(restTemplate);

            //Import transport key into keystore
            JWK jwk = application.getTransportKey();
            LOGGER.debug("jwk : {}", jwk);
            Certificate[] certChain = new Certificate[jwk.getX509CertChain().size()];
            for (int i = 0; i < jwk.getX509CertChain().size(); i++) {
                certChain[i] = CertificateUtils.decodeCertificate(jwk.getX509CertChain().get(i).decode());
            }
            AsymmetricJWK assymetricJWK = (AsymmetricJWK) jwk;
            LOGGER.debug("Add JWK into keystore under the alias {}", jwk.getKeyID());
            keyStoreService.getKeyStore().setKeyEntry(jwk.getKeyID(), assymetricJWK.toPrivateKey(),
                    keyStoreService.getKeyStorePassword().toCharArray(), certChain);
            keyStoreService.store();
            return application;
        }  catch (Exception e) {
            LOGGER.error("Couldn't get the current application from the jwkms", e);
            throw new RestTemplateLoadingException(e);
        }
    }
}

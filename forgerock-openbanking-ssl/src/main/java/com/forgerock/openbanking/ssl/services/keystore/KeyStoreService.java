/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.ssl.services.keystore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.security.KeyStoreException;

@Service
public class KeyStoreService extends KeyStoreFileService {
    private static final String JAVA_KEYSTORE = "JKS";

    @Value("${server.ssl.key-store}")
    private Resource keyStore;
    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;

    @Override
    public Resource getKeyStoreResource() {
        return keyStore;
    }

    @Override
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    @Override
    public KeyStore getKeyStoreInstance() throws KeyStoreException {
        return KeyStore.getInstance(JAVA_KEYSTORE);
    }
}

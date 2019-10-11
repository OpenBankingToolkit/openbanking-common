/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.ssl.config;

import com.forgerock.openbanking.ssl.exceptions.SslConfigurationFailure;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class SslConfiguration {

    private static final String JAVA_KEYSTORE = "jks";

    @Value("${server.ssl.key-store}")
    private Resource keyStore;
    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${server.ssl.key-password}")
    private String keyPassword;
    @Value("${server.ssl.enabled}")
    private boolean sslEnabled;

    public HttpComponentsClientHttpRequestFactory factory(String keyAlias, boolean checkHostname) throws SslConfigurationFailure {
        try {
            SSLContext sslContext = getSslContext(keyAlias);
            SSLConnectionSocketFactory socketFactory;

            if (checkHostname) {
                socketFactory = new SSLConnectionSocketFactory(sslContext);
            } else {
                socketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            }

            HttpClient httpClient = HttpClients.custom()
                    .setRedirectStrategy(new DefaultRedirectStrategy() {
                        @Override
                        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
                            return false;
                        }
                    })
                    .setSSLSocketFactory(socketFactory).build();

            return new HttpComponentsClientHttpRequestFactory(httpClient);
        } catch (Exception e) {
            throw new SslConfigurationFailure(e);
        }
    }

    public SSLContext getSslContext(String keyAlias) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, IOException, CertificateException, KeyManagementException {
        SSLContextBuilder sslContextBuilder;
        if (sslEnabled) {
            sslContextBuilder = new SSLContextBuilder()
                    .loadKeyMaterial(
                            getStore(keyStore.getURL(), keyStorePassword.toCharArray()),
                            keyPassword.toCharArray(),
                            (aliases, socket) -> keyAlias
                    );
        } else {
            sslContextBuilder = org.apache.http.ssl.SSLContexts.custom();
        }

        return sslContextBuilder.build();
    }

    public SslContext getSslContextForReactor(String keyAlias) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, CertificateException {
        KeyStore keyStore1 = getStore(keyStore.getURL(), keyStorePassword.toCharArray());
        X509Certificate certificate = (X509Certificate) keyStore1.getCertificate(keyAlias);
        PrivateKey key = (PrivateKey) keyStore1.getKey(keyAlias, keyPassword.toCharArray());
        SslContextBuilder sslContextBuilder = SslContextBuilder
                .forClient()
                .keyManager(key, certificate);

        return sslContextBuilder.build();
    }

    protected KeyStore getStore(final URL url, final char[] password) throws
            KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        final KeyStore store = KeyStore.getInstance(JAVA_KEYSTORE);
        InputStream inputStream = url.openStream();
        try {
            store.load(inputStream, password);
        } finally {
            inputStream.close();
        }

        return store;
    }
}

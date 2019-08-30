/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.ssl.services.keystore;

import com.forgerock.cert.eidas.EidasCertType;
import com.forgerock.cert.eidas.EidasInformation;
import com.forgerock.cert.exception.InvalidKeyType;
import com.forgerock.cert.utils.CertificateConfiguration;
import com.forgerock.cert.utils.CertificateUtils;
import com.forgerock.openbanking.ssl.model.csr.CSRGenerationResponse;
import com.forgerock.openbanking.ssl.utils.JwkUtils;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.KeyUse;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Representation of a keystore
 */
public abstract class KeyStoreFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreFileService.class);

    private KeyStore keyStore;

    /**
     * Get the JWK keystore
     * @return the keystore
     */
    public KeyStore getKeyStore() {
        if (keyStore == null) {
            try {
                keyStore = getStore(getKeyStoreResource().getURL(), getKeyStorePassword().toCharArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return keyStore;
    }

    public void store() {
        LOGGER.debug("Store the keystore stream into the keystore resource {}", getKeyStoreResource());
        try {
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(getKeyStoreResource().getURL().getFile());
                keyStore.store(outputStream, getKeyStorePassword().toCharArray());
                outputStream.flush();
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            LOGGER.debug("Flushed into the keystore");
        } catch (Exception e) {
            LOGGER.error("Couldn't save the keystore stream", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the keystore file.
     * @return
     */
    public abstract Resource getKeyStoreResource();

    /**
     * Get the keystore password.
     * @return
     */
    public abstract String getKeyStorePassword();

    public abstract KeyStore getKeyStoreInstance() throws KeyStoreException;

    public void importPem(String alias, String pem) throws CertificateException {
        ByteArrayInputStream inputStream = null;

        try {
            PrivateKey privateKey = (PrivateKey) getKeyStore().getKey(alias, getKeyStorePassword().toCharArray());
            inputStream = new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8.name()));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(inputStream);
            getKeyStore().setKeyEntry(alias, privateKey, getKeyStorePassword().toCharArray(),
                    new Certificate[] { cert });
        } catch (CertificateException | UnsupportedEncodingException | UnrecoverableKeyException
                | NoSuchAlgorithmException | KeyStoreException e) {
            LOGGER.error("Couldn't import pem {} for alias {}", pem, alias, e);
            throw new CertificateException(e);
        } finally {
            if (inputStream == null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Couldn't close properly ByteArrayOutputStream", e);
                }
            }
        }
    }


    /**
     * Generates a PKCS #10 Certification Request. This request is a signed structure that contains the subject name
     * to be associated with the public key that the Certification Request relates to. The request is signed with the
     * private key associated with the public key that the signature is being requested for. This  proves that the
     * requestor owns the private key associated with the public key that they are requesting be signed by the
     * Certificate Authority.
     * @param alias     Used to identify the key pair for which the Certification Request is being generated for
     * @param keyPair   The request will be used to generate a signed certificate that ties and identity to the public
     *                  key in this key pair. The Certification Request will be signed with the private key in this
     *                  keyPair. This serves as a Proof of Possession to the Certification Authority, proving that the
     *                  requester has access to the privateKey associated with the publicKey they wish to generate a
     *                  certificate for.
     * @param sigAlg    The algorithm used to sign the Certificate Request
     * @param certificateConfiguration The Identity that will become the subject of the requested certificate.
     * @param keyUse    The type of key being requested
     * @return - A {@link CSRGenerationResponse}
     *
     *
     * @throws CertificateException - When the Certificate Request could not be generated.
     */
    public CSRGenerationResponse generatePKCS10(String alias, KeyPair keyPair, Algorithm sigAlg,
                                                CertificateConfiguration certificateConfiguration, KeyUse keyUse)
            throws CertificateException {
        try {

            // generate PKCS10 certificate request
            X500Name x500Name = CertificateUtils.getX500Name(certificateConfiguration);
            //X500Principal principal = new X500Principal(x500Name.toString());

            PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                    x500Name, keyPair.getPublic());
            EidasInformation eidasInfo = certificateConfiguration.getEidasInfo();
            if(eidasInfo != null) {
                CertificateUtils.addEidasExtensionsToCSR(p10Builder, getEidasCertType(keyUse),
                        certificateConfiguration.getEidasInfo());
            }

            JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(JwkUtils.algorithmSignature(sigAlg));
            ContentSigner signer = csBuilder.build(keyPair.getPrivate());
            PKCS10CertificationRequest csr = p10Builder.build(signer);

            PemObject pemObject = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());
            StringWriter str = new StringWriter();
            JcaPEMWriter pemWriter = new JcaPEMWriter(str);
            pemWriter.writeObject(pemObject);
            pemWriter.close();
            str.close();

            CSRGenerationResponse csrGenerationResponse = new CSRGenerationResponse();
            csrGenerationResponse.setAlias(alias);
            csrGenerationResponse.setPkcs10CertificationRequest(csr);
            csrGenerationResponse.setCsr(str.toString());
            return csrGenerationResponse;
        } catch (OperatorCreationException | JOSEException  | IOException | InvalidKeyType e) {
            LOGGER.error("Couldn't generate CSR for alias {}", alias, alias, e);
            throw new CertificateException(e);
        }
    }

    protected KeyStore getStore(final URL url, final char[] password) {
        try {
            KeyStore outStore = getKeyStoreInstance();
            outStore.load(url.openStream(), password);
            return outStore;
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            LOGGER.error("Couldn't read keystore", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Convert from a nimbusds key use type to an EidasCertType.
     * @param keyUse an enum type specifying the use of the key
     * @return An EidasCertType enum that can be used to create the ASN.1 encoded eidas certificate type extension
     * for an eidas certificate.
     */
    public static EidasCertType getEidasCertType(KeyUse keyUse) throws InvalidKeyType {
        if (KeyUse.SIGNATURE.equals(keyUse)) {
            return EidasCertType.WEB;
        }
        if (KeyUse.ENCRYPTION.equals(keyUse)) {
            return EidasCertType.ESEAL;
        }
        throw new InvalidKeyType("Unrecognised key use: " + keyUse.toString());
    }
}

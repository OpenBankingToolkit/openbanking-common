/**
 * Copyright 2019 ForgeRock AS.
 *
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
package com.forgerock.openbanking.ssl.utils;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyType;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

public class JwkUtils {

    public static String algorithmSignature(final Algorithm alg) throws JOSEException {
        final String jcaAlg;

        PSSParameterSpec pssSpec = null;

        if (alg.equals(JWSAlgorithm.RS256)) {
            jcaAlg = "SHA256withRSA";
        } else if (alg.equals(JWSAlgorithm.RS384)) {
            jcaAlg = "SHA384withRSA";
        } else if (alg.equals(JWSAlgorithm.RS512)) {
            jcaAlg = "SHA512withRSA";
        } else if (alg.equals(JWSAlgorithm.PS256)) {
            jcaAlg = "SHA256withRSAandMGF1";
            // JWA mandates salt length must equal hash
            pssSpec = new PSSParameterSpec("SHA256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
        } else if (alg.equals(JWSAlgorithm.PS384)) {
            jcaAlg = "SHA384withRSAandMGF1";
            // JWA mandates salt length must equal hash
            pssSpec = new PSSParameterSpec("SHA384", "MGF1", MGF1ParameterSpec.SHA384, 48, 1);
        } else if (alg.equals(JWSAlgorithm.PS512)) {
            jcaAlg = "SHA512withRSAandMGF1";
            // JWA mandates salt length must equal hash
            pssSpec = new PSSParameterSpec("SHA512", "MGF1", MGF1ParameterSpec.SHA512, 64, 1);
        } else if (alg.equals(JWSAlgorithm.ES256)) {
            jcaAlg = "SHA256withECDSA";
        } else if (alg.equals(JWSAlgorithm.ES384)) {
            jcaAlg = "SHA384withECDSA";
        } else if (alg.equals(JWSAlgorithm.ES512)) {
            jcaAlg = "SHA512withECDSA";
        } else {
            //At this point we handled all the signing algorithms. It would only apply for encryption algorithms
            //TODO do the same than signing key and find the right algorithm depending of the encryption algorithm
            if (KeyType.forAlgorithm(alg) == KeyType.RSA) {
                jcaAlg = "SHA256WithRSA";
            } else if (KeyType.forAlgorithm(alg) == KeyType.EC) {
                jcaAlg = "SHA256withECDSA";
            } else {
                throw new JOSEException("Unsupported algorithm");
            }
        }
        return jcaAlg;
    }

    public static Signature getSignerAndVerifier(final Algorithm alg,
                                                    final Provider provider)
            throws JOSEException {

        // The JCE crypto provider uses different alg names

        final String jcaAlg;

        PSSParameterSpec pssSpec = null;

        if (alg.equals(JWSAlgorithm.RS256)) {
            jcaAlg = "SHA256withRSA";
        } else if (alg.equals(JWSAlgorithm.RS384)) {
            jcaAlg = "SHA384withRSA";
        } else if (alg.equals(JWSAlgorithm.RS512)) {
            jcaAlg = "SHA512withRSA";
        } else if (alg.equals(JWSAlgorithm.PS256)) {
            jcaAlg = "SHA256withRSAandMGF1";
            // JWA mandates salt length must equal hash
            pssSpec = new PSSParameterSpec("SHA256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
        } else if (alg.equals(JWSAlgorithm.PS384)) {
            jcaAlg = "SHA384withRSAandMGF1";
            // JWA mandates salt length must equal hash
            pssSpec = new PSSParameterSpec("SHA384", "MGF1", MGF1ParameterSpec.SHA384, 48, 1);
        } else if (alg.equals(JWSAlgorithm.PS512)) {
            jcaAlg = "SHA512withRSAandMGF1";
            // JWA mandates salt length must equal hash
            pssSpec = new PSSParameterSpec("SHA512", "MGF1", MGF1ParameterSpec.SHA512, 64, 1);
        } else if (alg.equals(JWSAlgorithm.ES256)) {
            jcaAlg = "SHA256withECDSA";
        } else if (alg.equals(JWSAlgorithm.ES384)) {
            jcaAlg = "SHA384withECDSA";
        } else if (alg.equals(JWSAlgorithm.ES512)) {
            jcaAlg = "SHA512withECDSA";
        } else
            //At this point we handled all the signing algorithms. It would only apply for encryption algorithms
            //TODO do the same than signing key and find the right algorithm depending of the encryption algorithm
            if (KeyType.forAlgorithm(alg) == KeyType.RSA) {
            jcaAlg = "SHA256WithRSA";
        } else if (KeyType.forAlgorithm(alg) == KeyType.EC) {
            jcaAlg = "SHA256withECDSA";
        } else {
            throw new JOSEException("Unsupported algorithm");
        }


        final Signature signature;
        try {
            if (provider != null) {
                signature = Signature.getInstance(jcaAlg, provider);
            } else {
                signature = Signature.getInstance(jcaAlg);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new JOSEException("Unsupported RSASSA algorithm: " + e.getMessage(), e);
        }


        if (pssSpec != null) {
            try {
                signature.setParameter(pssSpec);
            } catch (InvalidAlgorithmParameterException e) {
                throw new JOSEException("Invalid RSASSA-PSS salt length parameter: " + e.getMessage(), e);
            }
        }

        return signature;
    }
}

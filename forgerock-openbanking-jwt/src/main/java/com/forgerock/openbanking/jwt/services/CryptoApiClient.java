/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.jwt.services;

import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.model.CreateDetachedJwtResponse;
import com.forgerock.openbanking.jwt.model.SigningRequest;
import com.forgerock.openbanking.jwt.model.ValidDetachedJwtResponse;
import com.forgerock.openbanking.ssl.model.csr.CSRGenerationResponse;
import com.forgerock.openbanking.ssl.model.csr.CSRImportPemsRequest;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;

public interface CryptoApiClient {

    JWK getKey(String appId, String keyId) throws ParseException;

    String getPublicCert(String appId, String keyId);

    String getPrivateCert(String appId, String keyId);

    String signClaims(SigningRequest signingRequest, JWTClaimsSet jwtClaimsSet);

    String signClaims(String issuerId, JWTClaimsSet jwtClaimsSet, boolean includeKey);

    String signClaims(SigningRequest signingRequest, String issuerId, JWTClaimsSet jwtClaimsSet);

    String signClaims(RestTemplate restTemplate, SigningRequest signingRequest, String issuerId, JWTClaimsSet jwtClaimsSet);

    CreateDetachedJwtResponse signPayloadToDetachedJwt(RestTemplate restTemplate, SigningRequest signingRequest, String issuerId, String payload);

    CreateDetachedJwtResponse signPayloadToDetachedJwt(SigningRequest signingRequest, String issuerId, String payload);

    String signAndEncryptClaims(JWTClaimsSet jwtClaimsSet, String jwkUri) throws JOSEException;

    String signAndEncryptClaims(String issuerId, JWTClaimsSet jwtClaimsSet, String jwkUri) throws JOSEException;

    String signAndEncryptJwtForOBApp(JWTClaimsSet jwtClaimsSet, String obAppId) throws JOSEException;

    String signAndEncryptJwtForOBApp(String issuerId, JWTClaimsSet jwtClaimsSet, String obAppId) throws JOSEException;

    SignedJWT decryptJwe(String serializedJwe) throws ParseException, JOSEException;

    SignedJWT decryptJwe(String expectedAudienceId, String serializedJwe) throws ParseException, JOSEException;

    SignedJWT validateJws(String serializedJws, String expectedIssuerId) throws ParseException, InvalidTokenException;

    SignedJWT validateJwsWithExpectedAudience(String serializedJws, String expectedAudienceId, String expectedIssuerId) throws ParseException, InvalidTokenException;

    SignedJWT validateJws(String serializedJws, String expectedIssuerId, String jwkUri) throws ParseException, InvalidTokenException, IOException;

    SignedJWT validateJwsWithJWK(String serializedJws, String expectedIssuerId, String jwk) throws ParseException, InvalidTokenException;

    SignedJWT validateJws(String serializedJws, String expectedAudienceId, String expectedIssuerId, String jwkUri)
            throws ParseException, InvalidTokenException, IOException;

    ValidDetachedJwtResponse validateDetachedJWS(String jwsDetachedSignature, Object body, String expectedAudienceId, String expectedIssuerId, String jwkUri)
            throws InvalidTokenException, ParseException, IOException;

    ValidDetachedJwtResponse validateDetachedJWSWithJWK(String jwsDetachedSignature, Object body, String expectedAudienceId, String expectedIssuerId, JWK jwk)
            throws InvalidTokenException, ParseException, IOException;

    SignedJWT verifyAccessToken(String accessTokenBearer)
            throws ParseException, InvalidTokenException, IOException;
}

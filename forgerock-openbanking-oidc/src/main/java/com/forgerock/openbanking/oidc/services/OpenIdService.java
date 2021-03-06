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
package com.forgerock.openbanking.oidc.services;

import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OIDCException;
import com.forgerock.openbanking.jwt.config.JwtAuthConfigurationProperties;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.UserContext;
import com.forgerock.openbanking.model.claim.Claim;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.constants.OpenBankingConstants.JWT_BEARER_CLIENT_ASSERTION_TYPE;

@Service
public class OpenIdService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenIdService.class);
    private static final List<String> AM_AUTH_SCOPES = Arrays.asList(OpenBankingConstants.Scope.GROUP,
            OpenBankingConstants.Scope.OPENID, OpenBankingConstants.Scope.AUTHORITY );


    @Value("${ob.auth.oidc.idp.issuerid}")
    public String amIssuer;
    @Value("${ob.auth.oidc.idp.jwk-uri}")
    public String amJwkUri;
    @Value("${ob.auth.oidc.idp.authorize}")
    public String amAuthorize;
    @Value("${ob.auth.oidc.client.client-id:none}")
    public String clientId;
    @Autowired
    private JwtAuthConfigurationProperties jwtAuthConfigurationProperties;

    @Autowired
    private CryptoApiClient cryptoApiClient;

    public AccessTokenResponse passwordGrantFlow(AMGateway amGateway, String amAccessTokenEndpoint, String username, String password) throws OIDCException {
        String clientAuthenticationJwt = generateClientAuthenticationJwt(clientId, amIssuer);

        //Request body
        LOGGER.debug("We do a password grant flow for the username {}.", username);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set(OIDCConstants.OIDCClaim.GRANT_TYPE, OIDCConstants.GrantType.PASSWORD.type);
        params.set(OIDCConstants.OIDCClaim.USERNAME, username);
        params.set(OIDCConstants.OIDCClaim.PASSWORD, password);
        params.set(OIDCConstants.OIDCClaim.SCOPE, AM_AUTH_SCOPES.stream().collect(Collectors.joining(" ")));

        LOGGER.debug("We authenticate to the AS via the client authentication JWT");
        /* we authenticate to the AS via the client authentication JWT.*/
        params.set(OIDCConstants.OIDCClaim.CLIENT_ASSERTION_TYPE, JWT_BEARER_CLIENT_ASSERTION_TYPE);
        params.set(OIDCConstants.OIDCClaim.CLIENT_ASSERTION, clientAuthenticationJwt);
        LOGGER.debug("Client credential JWT : '{}'",clientAuthenticationJwt);

        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //Send request
        LOGGER.debug("Send new authorization code request to OpenAM.");
        try {
            return (AccessTokenResponse) amGateway.toAM(amAccessTokenEndpoint, HttpMethod.POST, headers,
                    new ParameterizedTypeReference<AccessTokenResponse>() {}, params).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST && e.getResponseBodyAsString().contains("authentication failed")) {
                throw new OIDCException(e.getResponseBodyAsString(), e);
            }
            LOGGER.error("Password grant flow failed: {}", e.getResponseBodyAsString(), e);
            throw e;
        }
    }

    public String generateAuthorisationRequest(String state, String redirectUri, List<String> scopes, List<String> acrValues) {
        String requestParameter = generateRequestParameter(state, state, redirectUri, scopes, acrValues);
        return authorisationCodeFlow(state, state, requestParameter, redirectUri, scopes);
    }

    public AccessTokenResponse exchangeCode(AMGateway amGateway, String redirectUri, String amAccessTokenEndpoint, String code) throws OIDCException {

        //Request body
        LOGGER.debug("We exchange the code '{}' for an access token.", code);
        String clientAuthenticationJwt = generateClientAuthenticationJwt(clientId, amIssuer);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set(OIDCConstants.OIDCClaim.GRANT_TYPE, OIDCConstants.GrantType.AUTHORIZATION_CODE.type);
        params.set(OIDCConstants.OIDCClaim.CODE, code);
        params.set(OIDCConstants.OIDCClaim.REDIRECT_URI, redirectUri);

        LOGGER.debug("We authenticate to the AS via the client authentication JWT");
        /* we authenticate to the AS via the client authentication JWT.*/
        params.set(OIDCConstants.OIDCClaim.CLIENT_ASSERTION_TYPE, JWT_BEARER_CLIENT_ASSERTION_TYPE);
        params.set(OIDCConstants.OIDCClaim.CLIENT_ASSERTION, clientAuthenticationJwt);
        LOGGER.debug("Client credential JWT : '{}'",clientAuthenticationJwt);

        //Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //Send request
        LOGGER.debug("Exchange code to OpenAM.");
        try {
            return (AccessTokenResponse) amGateway.toAM(amAccessTokenEndpoint, HttpMethod.POST, headers,
                    new ParameterizedTypeReference<AccessTokenResponse>() {}, params).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST && e.getResponseBodyAsString().contains("authentication failed")) {
                throw new OIDCException(e.getResponseBodyAsString(), e);
            }
            LOGGER.error("Exchange code failed: {}", e.getResponseBodyAsString(), e);
            throw e;
        }
    }

    /**
     * Generate a new client authentication JWT.
     *
     * @return a JWT that can be used to authenticate Kyle to the AS.
     */
    public String generateClientAuthenticationJwt(String clientId, String amIssuer) {
        JWTClaimsSet.Builder requestParameterClaims;
        requestParameterClaims = new JWTClaimsSet.Builder();
        //By putting the issuer id as subject, this JWT will play the role of client credential. Never generate
        // another JWT with client id as subject! Otherwise this JWT can be used as credential as well!
        requestParameterClaims.subject(clientId);
        requestParameterClaims.audience(amIssuer);
        requestParameterClaims.expirationTime(new Date(new Date().getTime() + Duration.ofMinutes(10).toMillis()));
        return cryptoApiClient.signClaims(clientId, requestParameterClaims.build(), false);
    }


    /**
     * Generate a new request parameter. Only needed when the signature and encryption keys needs to be rotated.
     *
     * @return the request parameter JWT, containing some claims.
     * @throws JOSEException
     */
    public String generateRequestParameter(String state, String nonce, String redirectUri, List<String> scopes, List<String> acrValues) {
        //Span are needed for doing a Zipkin tracing.
        LOGGER.debug("Generate a request parameter for th state '{}' and nonce '{}'",
                state, nonce);
        JWTClaimsSet.Builder requestParameterClaims;
        requestParameterClaims = new JWTClaimsSet.Builder();
        requestParameterClaims.audience(amIssuer);
        requestParameterClaims.expirationTime(new Date(new Date().getTime() + Duration.ofHours(1).toMillis()));
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.RESPONSE_TYPE, OIDCConstants.ResponseType.CODE);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.CLIENT_ID, clientId);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.REDIRECT_URI, redirectUri);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.SCOPE, scopes.stream().collect(Collectors.joining(" ")));
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.STATE, state);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.NONCE, nonce);
        requestParameterClaims.claim(OIDCConstants.OIDCClaim.MAX_AGE, "10"); //a 10 secs timelife allows to not have a session in AM. This makes the logout easier

        if (!acrValues.isEmpty()) {
            //We will ask some claims and will do a policy enforcement by using the acr essential claim.
            JSONObject claims = new JSONObject();
            JSONObject idTokenClaims = new JSONObject();
            idTokenClaims.put(OpenBankingConstants.IdTokenClaim.ACR, new Claim(true, acrValues).toJson());
            claims.put(OpenBankingConstants.IdTokenClaim.ID_TOKEN, idTokenClaims);
            requestParameterClaims.claim(OIDCConstants.OIDCClaim.CLAIMS, claims);
        }
        LOGGER.debug("Request parameter JWS : '{}'",
                cryptoApiClient.signClaims(clientId, requestParameterClaims.build(), false));

        return cryptoApiClient.signClaims(clientId, requestParameterClaims.build(), false);
    }


    /**
     * Get an access token via the client credential flow
     *
     * @return the redirect uri
     */
    public String authorisationCodeFlow(String state, String nonce, String requestParameter,
                             String redirectUri, List<String> scopes) {
        LOGGER.debug("Start the hybrid flow by redirecting the user to the authorize endpoint");
        //Request body
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(amAuthorize);

        builder.queryParam(OIDCConstants.OIDCClaim.RESPONSE_TYPE, OIDCConstants.ResponseType.CODE);
        builder.queryParam(OIDCConstants.OIDCClaim.CLIENT_ID, clientId);
        builder.queryParam(OIDCConstants.OIDCClaim.STATE, state);
        builder.queryParam(OIDCConstants.OIDCClaim.NONCE, nonce);
        builder.queryParam(OIDCConstants.OIDCClaim.SCOPE, scopes.stream().collect(Collectors.joining(" ")));
        builder.queryParam(OIDCConstants.OIDCClaim.REDIRECT_URI, redirectUri);
        builder.queryParam(OIDCConstants.OIDCClaim.REQUEST, requestParameter);

        return builder.build().encode().toUriString();
    }

    public UserContext fromIdToken(String idToken) throws ParseException, InvalidTokenException, IOException {
        cryptoApiClient.validateJws(idToken, jwtAuthConfigurationProperties.getExpectedAudienceId(),
                jwtAuthConfigurationProperties.getExpectedIssuerId(), jwtAuthConfigurationProperties.getJwkUri());

        return UserContext.createOIDCClient(idToken);
    }

    public UserContext fromIdToken(String idToken, X509Certificate[] certs) throws ParseException, InvalidTokenException, IOException {
        cryptoApiClient.validateJws(idToken, jwtAuthConfigurationProperties.getExpectedAudienceId(),
                jwtAuthConfigurationProperties.getExpectedIssuerId(), jwtAuthConfigurationProperties.getJwkUri());

        return UserContext.createOIDCClient(idToken, certs);
    }
}

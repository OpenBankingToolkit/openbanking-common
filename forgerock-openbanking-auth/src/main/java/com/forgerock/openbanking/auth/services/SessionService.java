/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services;

import com.forgerock.cert.SubjectHash;
import com.forgerock.openbanking.am.gateway.AMGateway;
import com.forgerock.openbanking.analytics.model.entries.SessionCounterType;
import com.forgerock.openbanking.analytics.services.SessionCountersKPIService;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorAuthenticationException;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OIDCException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.UserContext;
import com.forgerock.openbanking.model.UserGroup;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import com.forgerock.openbanking.oidc.services.OpenIdService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.security.cert.CertificateEncodingException;
import java.text.ParseException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.forgerock.openbanking.constants.AMConstants.SSOClaim.MTLS_SUBJECT_HASH;

@Service
@Slf4j
public class SessionService {

    @Autowired
    private CryptoApiClient cryptoApiClient;
    @Value("${session.issuer-id}")
    private String issuerId;
    @Value("${session.token-lifetime}")
    private Integer sessionLifeTime;
    @Autowired
    private SessionCountersKPIService sessionCountersKPIService;
    @Autowired
    private OpenIdService openIdService;

    /**
     * Get an expired session, useful for logout a user
     * @param userContext the user context
     * @return a user token with an expiration negatives
     */
    public String expiredSessionContext(UserContext userContext) {
        JWTClaimsSet.Builder sessionClaims  = new JWTClaimsSet.Builder()
                .issuer(issuerId)
                .audience(issuerId)
                .subject(userContext.getUsername())
                .expirationTime(new Date(0))
                .claim(OpenBankingConstants.SSOClaim.AUTHORITIES, userContext.getAuthorities());

        return cryptoApiClient.signAndEncryptJwtForOBApp(sessionClaims.build(), issuerId);
    }

    /**
     * Generate a session with a specific lifetime
     * @param userContext
     * @return a  session with the corresponding lifetime
     */
    public String generateSessionContextJwt(UserContext userContext) throws CertificateEncodingException {
        JWTClaimsSet.Builder sessionClaims  = new JWTClaimsSet.Builder(userContext.getSessionClaims())
                .issuer(issuerId)
                .audience(issuerId)
                .subject(userContext.getUsername())
                .expirationTime(new Date(new Date().getTime() + Duration.ofDays(sessionLifeTime).toMillis()))
                .claim(OpenBankingConstants.SSOClaim.AUTHORITIES, userContext.getAuthorities());

        Optional<String> certHashcode = SubjectHash.hash(userContext.getCertificatesChain());
        certHashcode.ifPresent(hash -> sessionClaims.claim(MTLS_SUBJECT_HASH, hash));
        return cryptoApiClient.signAndEncryptJwtForOBApp(sessionClaims.build(), issuerId);
    }

    /**
     * Get the user context from a session
     * @param sessionJwtSerialised session as a JWE(JWS)
     * @return the user context
     * @throws ParseException
     */
    public UserContext getUserContext(String sessionJwtSerialised) throws ParseException {
        SignedJWT sessionJws = cryptoApiClient.decryptJwe(issuerId, sessionJwtSerialised);
        if (sessionJws.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            log.debug("Token {} as expired {}", sessionJws.serialize(), sessionJws.getJWTClaimsSet().getExpirationTime());
            throw new OBErrorAuthenticationException(OBRIErrorType.SESSION_TOKEN_EXPIRED);
        }
        String username = sessionJws.getJWTClaimsSet().getSubject();
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String authority : sessionJws.getJWTClaimsSet().getStringListClaim(OpenBankingConstants.SSOClaim.AUTHORITIES)) {
            try {
                roles.add(OBRIRole.valueOf(authority));
                continue;
            } catch (IllegalArgumentException e) {
            }
            try {
                roles.add(UserGroup.valueOf(authority));
                continue;
            } catch (IllegalArgumentException ignored) {
            }
            log.warn("Couldn't de-serialised authority '{}' to OBRI role or group", authority);

        }
        roles.add(OBRIRole.ROLE_USER);

        return UserContext.createOIDCClient(username, new ArrayList<>(roles), sessionJws.getJWTClaimsSet());
    }


    /**
     * Authenticate with password grant flow
     * @param sessionType which type of session is being created
     * @param amGateway AM gateway for auth or bank realm
     * @param amAccessTokenEndpoint
     * @return Session token mean't for the end user
     * @throws OIDCException password grant flow fails
     */
    public String authenticate(@RequestParam("username") String username, @RequestParam("password") String password,
                               Authentication principal, SessionCounterType sessionType, AMGateway amGateway,
                               String amAccessTokenEndpoint) throws OIDCException, OBErrorException {
        try {
            UserContext userContextFromMatls = (UserContext) principal.getPrincipal();
            AccessTokenResponse accessTokenResponse = openIdService.passwordGrantFlow(amGateway, amAccessTokenEndpoint, username, password);
            log.info("The access token response : {}", accessTokenResponse);
            UserContext userContext = openIdService.fromIdToken(accessTokenResponse.getIdToken(), userContextFromMatls.getCertificatesChain());

            sessionCountersKPIService.incrementSessionCounter(sessionType);
            List<GrantedAuthority> mergedAuthorities = Stream.concat(userContext.getAuthorities().stream(), userContextFromMatls.getAuthorities().stream())
                    .distinct()
                    .collect(Collectors.toList());
            return generateSessionContextJwt(UserContext.createOIDCClient(userContext.getUsername(),
                    mergedAuthorities, userContext.getSessionClaims(), userContext.getCertificatesChain()));
        } catch (HttpClientErrorException e) {
            log.error("AM exception: {}", e.getResponseBodyAsString(), e);
        } catch (ParseException e) {
            log.error("Can't parse ID token", e);
        } catch (InvalidTokenException e) {
            log.error("ID Token is invalid", e);
        } catch (CertificateEncodingException e) {
            log.error("Certificate exception", e);
        }
        throw new OBErrorException(OBRIErrorType.SERVER_ERROR, "Couldn't authenticate the user");
    }
}

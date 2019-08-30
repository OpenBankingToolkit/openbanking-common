/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services;

import com.forgerock.cert.SubjectHash;
import com.forgerock.openbanking.auth.config.JwtAuthConfigurationProperties;
import com.forgerock.openbanking.auth.error.JwtAuthenticationFailureHandler;
import com.forgerock.openbanking.auth.exceptions.OBErrorAuthenticationException;
import com.forgerock.openbanking.auth.model.JwtAuthenticationToken;
import com.forgerock.openbanking.auth.model.OBRIRole;
import com.forgerock.openbanking.auth.model.UserContext;
import com.forgerock.openbanking.auth.model.error.OBRIErrorType;
import com.forgerock.openbanking.auth.services.oidc.UserInfoService;
import com.forgerock.openbanking.core.services.CryptoApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.cert.CertificateEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.forgerock.openbanking.auth.constants.AMConstants.SSOClaim.MTLS_SUBJECT_HASH;


@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    @Autowired
    private CryptoApiClient cryptoApiClient;
    @Autowired
    private JwtAuthConfigurationProperties jwtAuthConfigurationProperties;
    @Autowired
    private UserInfoService userInfoService;
    @Resource(name = "forExternalForgeRockApplication")
    private RestTemplate restTemplate;
    @Autowired
    private SessionService sessionService;

    @Override
    public JwtAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationFailureHandler.RawAccessJwtToken rawAccessToken = (JwtAuthenticationFailureHandler.RawAccessJwtToken) authentication.getCredentials();
        if (rawAccessToken == null || rawAccessToken.getToken() == null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(OBRIRole.ROLE_ANONYMOUS);
            UserContext context = UserContext.create("anonymous", authorities, UserContext.UserType.ANONYMOUS);
            return new JwtAuthenticationToken(context, context.getAuthorities());
        }

        switch (rawAccessToken.getType()) {

            case SSO_TOKEN:
                String ssoToken = rawAccessToken.getToken();
                try {
                    UserContext userContext = sessionService.getUserContext(ssoToken);
                    String sessionMtlsSubjectHash = userContext.getSessionClaims().getStringClaim(MTLS_SUBJECT_HASH);
                    if (sessionMtlsSubjectHash != null)
                        verifyCertificateBinding((JwtAuthenticationToken) authentication, sessionMtlsSubjectHash);

                    return new JwtAuthenticationToken(userContext, userContext.getAuthorities());
                } catch (ParseException e) {
                    LOGGER.debug("Invalid sso token token format {}: {}", ssoToken, e);
                    throw new OBErrorAuthenticationException(OBRIErrorType.SESSION_TOKEN_INVALID_FORMAT);
                } catch (CertificateEncodingException e) {
                    LOGGER.debug("Invalid sso token token format {}: {}", ssoToken, e);
                    throw new OBErrorAuthenticationException(OBRIErrorType.TPP_REGISTRATION_UNKNOWN_TRANSPORT_CERTIFICATE);
                }
            case ACCESS_TOKEN:
            case REFRESH_TOKEN:
                break;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(OBRIRole.ROLE_ANONYMOUS);
        UserContext context = UserContext.create("anonymous", authorities, UserContext.UserType.ANONYMOUS);
        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    private void verifyCertificateBinding(JwtAuthenticationToken authentication, String sessionMtlsSubjectHash) throws CertificateEncodingException {
        Optional<String> certMtlsSubjectHash = SubjectHash.hash(authentication.getClientCerts());
        String certMtlsSubjectHashValue = certMtlsSubjectHash.orElseThrow(() -> {
            LOGGER.debug("{} in session but MTLS not used in request", MTLS_SUBJECT_HASH);
            return new OBErrorAuthenticationException(OBRIErrorType.CLIENT_CERTIFICATE_NOT_PROVIDED);
        });
        if (!certMtlsSubjectHashValue.equals(sessionMtlsSubjectHash)) {
            LOGGER.debug("Client certificate doesn't match certificate subject hash from user session clientCertHash={} sessionCertHash={}",
                    certMtlsSubjectHash, sessionMtlsSubjectHash);
            throw new OBErrorAuthenticationException(OBRIErrorType.CLIENT_CERTIFICATE_NOT_MATCH);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

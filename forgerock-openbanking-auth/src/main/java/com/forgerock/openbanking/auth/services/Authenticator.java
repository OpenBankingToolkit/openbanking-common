/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services;

import com.forgerock.openbanking.auth.config.JwtAuthConfigurationProperties;
import com.forgerock.openbanking.auth.error.JwtAuthenticationFailureHandler;
import com.forgerock.openbanking.auth.model.JwtAuthenticationToken;
import com.forgerock.openbanking.auth.model.OBRIRole;
import com.forgerock.openbanking.auth.model.UserContext;
import com.forgerock.openbanking.auth.services.cookie.CookieService;
import com.forgerock.openbanking.auth.services.x509.ForgeRockAppMATLService;
import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class Authenticator {

    static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    static final String CLIENT_JWK_HEADER_NAME = "X-Client-Jwk";

    private final JwtAuthConfigurationProperties jwtAuthConfigurationProperties;
    private final UserDetailsService userDetailsService;

    public Authenticator(JwtAuthConfigurationProperties jwtAuthConfigurationProperties,
                         UserDetailsService userDetailsService) {
        this.jwtAuthConfigurationProperties = jwtAuthConfigurationProperties;
        this.userDetailsService = userDetailsService;
    }

    public Authentication authenticate(HttpServletRequest request, AuthenticationManager authenticationManager) {
        log.trace("Attempt authentication");
        // 1) Check the cookie
        log.trace("1) Try cookie authentication");
        Authentication authentication = tryCookieAuthentication(request, authenticationManager);
        if (authentication != null) {
            return authentication;
        }

        // 2) Check the X509 auth filter did not authenticate it already
        authentication = SecurityContextHolder.getContext().getAuthentication();
        log.trace("2) verify if it is authenticated already via X509");
        if (isCertificateAuthenticated(authentication)) {
            return authentication;
        }

        // 3) Authentication from the JWK header
        log.trace("3) Try JWK header authentication");
        Authentication jwkHeaderAuthentication = getJwkHeaderAuthentication(request);
        if (jwkHeaderAuthentication != null) {
            return jwkHeaderAuthentication;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(OBRIRole.ROLE_ANONYMOUS);
        UserContext context = UserContext.create("anonymous", authorities, UserContext.UserType.ANONYMOUS);
        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    private Authentication tryCookieAuthentication(HttpServletRequest request, AuthenticationManager authenticationManager) throws AuthenticationException {
        if (jwtAuthConfigurationProperties.isCookie()) {
            log.trace("Looking for cookies");
            Cookie cookie = getCookie(request, CookieService.SESSION_CONTEXT_COOKIE_NAME);
            if (cookie != null) {
                log.trace("Found cookie received {}", cookie.getValue());
                try {
                    JwtAuthenticationFailureHandler.RawAccessJwtToken token = new JwtAuthenticationFailureHandler.RawAccessJwtToken(
                            JwtAuthenticationFailureHandler.RawAccessJwtToken.Type.SSO_TOKEN, cookie.getValue());

                    String clientJWK = request.getHeader(CLIENT_JWK_HEADER_NAME);
                    Optional<JWK> jwk = getJwkFromHeader(clientJWK);
                    List<X509Certificate> certs = jwk.isPresent() ? jwk.get().getParsedX509CertChain() : Collections.emptyList();
                    return authenticationManager.authenticate(new JwtAuthenticationToken(token, certs.toArray(new X509Certificate[]{})));
                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
                        throw new BadCredentialsException("Invalid cookie");
                    }
                    throw e;
                }
            } else {
                log.trace("No cookie found");
            }
        } else {
            log.trace("Cookie authentication disabled");
        }
        return null;
    }

    private Authentication getJwkHeaderAuthentication(HttpServletRequest request) {
        String clientJWK = request.getHeader(CLIENT_JWK_HEADER_NAME);
        Optional<JWK> jwk = getJwkFromHeader(clientJWK);
        if (jwk.isPresent()) {
            X509Certificate[] certificatesChain = jwk.get().getParsedX509CertChain().toArray(new X509Certificate[0]);
            Optional<UserContext> isUserDetails = ((ForgeRockAppMATLService) userDetailsService).isInternalForgeRockApp(certificatesChain);
            if (isUserDetails.isPresent()) {
                return new UsernamePasswordAuthenticationToken(isUserDetails.get(), "", isUserDetails.get().getAuthorities());
            }
        }

        Optional<UserContext> isUserDetails = ((ForgeRockAppMATLService) userDetailsService).isExternalForgeRockAppFromJWKHeader(clientJWK);
        if (isUserDetails.isPresent()) {
            return new UsernamePasswordAuthenticationToken(isUserDetails.get(), "", isUserDetails.get().getAuthorities());
        } else {
            log.trace("No bearer token found");
        }
        return null;
    }

    private Optional<JWK> getJwkFromHeader(String clientJWK) {
        if (clientJWK == null || clientJWK.equals("")) {
            log.trace("No bearer token found");
            return Optional.empty();
        }
        log.trace("Client JWK received {}", clientJWK);
        if (userDetailsService instanceof ForgeRockAppMATLService) try {
            return Optional.of(JWK.parse(clientJWK));
        } catch (ParseException e) {
            log.error("Can't parse jwk from certificate '{}'", clientJWK, e);
            return Optional.empty();
        }
        return Optional.empty();
    }

    private boolean isCertificateAuthenticated(Authentication authentication) {
        if (authentication != null) {
            log.trace("Authenticated via X509: {}", authentication);
            Collection<? extends GrantedAuthority> authorities = ((UserDetails) authentication.getPrincipal()).getAuthorities();
            if (!authorities.contains(new SimpleGrantedAuthority(OBRIRole.ROLE_ANONYMOUS.name()))) {
                return true;
            }
        }
        log.trace("It's an anonymous");
        return false;
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}

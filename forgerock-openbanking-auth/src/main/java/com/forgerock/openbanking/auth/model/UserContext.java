/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model;

import com.forgerock.cert.Psd2CertInfo;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserContext implements UserDetails {

    private String username;
    private List<GrantedAuthority> authorities;
    private JWTClaimsSet claims;
    private UserType userType;
    private X509Certificate[] certificatesChain;
    private Psd2CertInfo psd2CertInfo;

    public static UserContext create(String username, List<GrantedAuthority> authorities, UserType userType) {
        if ("".equals(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return UserContext.builder().username(username).claims(new JWTClaimsSet.Builder().build()).authorities(authorities).userType(userType).build();
    }

    public static UserContext create(String username, List<GrantedAuthority> authorities, UserType userType,  X509Certificate[] certificatesChain) {
        if ("".equals(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return UserContext.builder()
                .username(username)
                .claims(new JWTClaimsSet.Builder().build())
                .authorities(authorities)
                .userType(userType)
                .certificatesChain(certificatesChain)
                .build();
    }

    public static UserContext create(String username, List<GrantedAuthority> authorities, UserType userType,
                                     Psd2CertInfo psd2CertInfo,  X509Certificate[] certificatesChain) {
        if ("".equals(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return UserContext.builder()
                .username(username)
                .claims(new JWTClaimsSet.Builder().build())
                .authorities(authorities)
                .userType(userType)
                .certificatesChain(certificatesChain)
                .psd2CertInfo(psd2CertInfo)
                .build();
    }

    public static UserContext createOIDCClient(String username, List<GrantedAuthority> authorities, JWTClaimsSet claims) {
        if ("".equals(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return UserContext.builder().username(username).authorities(authorities).claims(claims).userType(UserType.OIDC_CLIENT).build();
    }

    public static UserContext createOIDCClient(String idToken) throws ParseException {
        return fromIdToken(idToken).build();
    }

    public static UserContext createOIDCClient(String idToken, X509Certificate[] certs) throws ParseException {
        return fromIdToken(idToken).certificatesChain(certs).build();
    }

    private static UserContextBuilder fromIdToken(String idToken) throws ParseException {
        SignedJWT idtokenJws = (SignedJWT) JWTParser.parse(idToken);
        log.trace("ID token valid. Subject: {}", idtokenJws.getJWTClaimsSet().getSubject());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(OBRIRole.ROLE_ID_TOKEN);
        log.trace("Add role 'id token'");


        log.trace("Add the other user role");
        if (idtokenJws.getJWTClaimsSet().getClaim("group") != null) {
            if (idtokenJws.getJWTClaimsSet().getClaim("group") instanceof String) {
                UserGroup group = UserGroup.valueFromGroupeId(idtokenJws.getJWTClaimsSet().getStringClaim("group"));
                if (group != null) {
                    log.debug("Add role '{}'", group);
                    authorities.add(group);
                }
            } else {
                for (String groupId : idtokenJws.getJWTClaimsSet().getStringArrayClaim("group")) {
                    UserGroup group = UserGroup.valueFromGroupeId(groupId);
                    if (group != null) {
                        log.debug("Add role '{}'", group);
                        authorities.add(group);
                    }
                }
            }
        }

        log.trace("Add the user authority");
        if (idtokenJws.getJWTClaimsSet().getClaim("authority") != null) {
            if (idtokenJws.getJWTClaimsSet().getClaim("authority") instanceof String) {
                OBRIAuthorities authority = OBRIAuthorities.valueFromId(idtokenJws.getJWTClaimsSet().getStringClaim("authority"));
                if (authority != null) {
                    log.debug("Add authority '{}'", authority);
                    authorities.add(authority.getRole());
                }
            } else {
                for (String groupId : idtokenJws.getJWTClaimsSet().getStringArrayClaim("authority")) {
                    OBRIAuthorities authority = OBRIAuthorities.valueFromId(groupId);
                    if (authority != null) {
                        log.debug("Add authority '{}'", authority);
                        authorities.add(authority.getRole());
                    }
                }
            }
        }
        return UserContext.builder().username(idtokenJws.getJWTClaimsSet().getSubject().toLowerCase()).authorities(authorities).claims(idtokenJws.getJWTClaimsSet()).userType(UserType.OIDC_CLIENT);
    }

    public static UserContext createOIDCClient(String username, List<GrantedAuthority> authorities, JWTClaimsSet claims, X509Certificate[] certificatesChain) {
        return UserContext.builder().username(username).authorities(authorities).claims(claims).userType(UserType.OIDC_CLIENT).certificatesChain(certificatesChain).build();
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }

    public JWTClaimsSet getSessionClaims() {
        return claims;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Psd2CertInfo getPsd2CertInfo() {
        return psd2CertInfo;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public X509Certificate[] getCertificatesChain() {
        return certificatesChain;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public enum UserType {
        SOFTWARE_STATEMENT,
        OIDC_CLIENT,
        JWKMS_APPLICATION,
        UPGRADE,
        EIDAS,
        MONITORING, GATEWAY, ANONYMOUS
    }

}

/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.x509;


import com.forgerock.cert.Psd2CertInfo;
import com.forgerock.cert.exception.InvalidPsd2EidasCertificate;
import com.forgerock.cert.utils.CertificateUtils;
import com.forgerock.openbanking.auth.config.MATLSConfigurationProperties;
import com.forgerock.openbanking.auth.model.ApplicationIdentity;
import com.forgerock.openbanking.auth.model.OBRIRole;
import com.forgerock.openbanking.auth.model.UserContext;
import com.forgerock.openbanking.auth.services.directory.DirectoryService;
import com.forgerock.openbanking.ssl.services.keystore.KeyStoreService;
import com.forgerock.openbanking.ssl.utils.RequestUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyStoreException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
public class ForgeRockAppMATLService implements UserDetailsService {

    public Resource forgerockSelfSignedRootCertificatePem;
    //TODO remove directory dependency
    private DirectoryService directoryService;
    private KeyStoreService keyStoreService;
    private MATLSConfigurationProperties matlsConfigurationProperties;
    private String clientJwkHeader;

    @Autowired
    public ForgeRockAppMATLService(@Value("${certificates.selfsigned.forgerock.root}") Resource forgerockSelfSignedRootCertificatePem,
                                   @Value("${gateway.client-jwk-header}") String clientJwkHeader,
                                   DirectoryService directoryService,
                                   KeyStoreService keyStoreService,
                                   MATLSConfigurationProperties matlsConfigurationProperties
                                   ){
        this.forgerockSelfSignedRootCertificatePem = forgerockSelfSignedRootCertificatePem;
        this.clientJwkHeader = clientJwkHeader;
        this.directoryService = directoryService;
        this.keyStoreService = keyStoreService;
        this.matlsConfigurationProperties = matlsConfigurationProperties;
    }

    @Override
    public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {

        log.trace("Load user from certificate subject {}", subject);
        if (RequestContextHolder.getRequestAttributes() == null) {
            log.warn("No request attributes available!");
            return UserContext.create("No-Request", Collections.singletonList(OBRIRole.ROLE_ANONYMOUS), UserContext.UserType.ANONYMOUS);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        if (request == null) {
            log.warn("No request received!");
            return UserContext.create("No-Request", Collections.singletonList(OBRIRole.ROLE_ANONYMOUS), UserContext.UserType.ANONYMOUS);
        }

        //Check if it's an external ForgeRock app
        Optional<UserContext> isUser = isExternalForgeRockAppFromJWKHeader(request.getHeader(clientJwkHeader));
        if (isUser.isPresent()) {
            return isUser.get();
        }

        X509Certificate[] certificatesChain = RequestUtils.extractCertificatesChain(request);

        //Check if no client certificate received
        if (certificatesChain == null) {
            log.debug("No certificate received");
            return UserContext.create("Anonymous", Collections.singletonList(OBRIRole.ROLE_ANONYMOUS), UserContext.UserType.ANONYMOUS);
        }
        //Check if it's an internal ForgeRock app
        isUser = isInternalForgeRockApp(certificatesChain);
        if (isUser.isPresent()) {
            return isUser.get();
        }

        //Check if it's an external ForgeRock app
        isUser = isExternalForgeRockAppFromCert(certificatesChain);
        return isUser.orElse(UserContext.create("Anonymous", Collections.singletonList(OBRIRole.ROLE_ANONYMOUS), UserContext.UserType.ANONYMOUS));
    }

    public Optional<UserContext> isInternalForgeRockApp(X509Certificate[] certificatesChain) {
        try {
            X509Certificate caCertificate = (X509Certificate) keyStoreService.getKeyStore().getCertificate(matlsConfigurationProperties.getForgerockInternalCAAlias());

            if ((certificatesChain.length > 1 && caCertificate.equals(certificatesChain[1]))
                    || (certificatesChain.length == 1 && caCertificate.getSubjectX500Principal().equals(certificatesChain[0].getIssuerX500Principal()))) {

                X509Certificate certificate = certificatesChain[0];
                X500Name x500name = new JcaX509CertificateHolder(certificate).getSubject();
                RDN cn = x500name.getRDNs(BCStyle.CN)[0];
                String cnString = IETFUtils.valueToString(cn.getFirst().getValue());

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(OBRIRole.ROLE_FORGEROCK_INTERNAL_APP);
                UserContext.UserType userType = UserContext.UserType.ANONYMOUS;
                if (matlsConfigurationProperties.getForgerockGatewaySubIds().contains(cnString)) {
                    authorities.add(OBRIRole.ROLE_GATEWAY);
                    userType = UserContext.UserType.GATEWAY;
                }
                if (matlsConfigurationProperties.getMonitoringAppsIds().contains(cnString)) {
                    authorities.add(OBRIRole.ROLE_MONITORING);
                    userType = userType.MONITORING;
                }
                return Optional.of(UserContext.create(cnString, authorities, userType));
            }
        } catch (KeyStoreException e) {
            log.error("Can't get ForgeRock internal CA");
        } catch (CertificateEncodingException e) {
            log.error("Can't get CN", e);
        }
        return Optional.empty();
    }

    public Optional<UserContext> isExternalForgeRockAppFromJWKHeader(String clientJWK) {
        if (clientJWK != null && !"".equals(clientJWK)) {
            UserContext userDetails;

            JWK jwk;
            try {
                jwk = JWK.parse(clientJWK);
                X509Certificate[] certChain = new X509Certificate[jwk.getX509CertChain().size()];
                for (int i = 0; i < jwk.getX509CertChain().size(); i++) {
                    certChain[i] = CertificateUtils.decodeCertificate(jwk.getX509CertChain().get(i).decode());
                }
                userDetails = getUserDetails(jwk, certChain);
            } catch (ParseException e) {
                log.error("Can't parse jwk from certificate '{}'", clientJWK, e);
                return Optional.empty();
            } catch (CertificateException e) {
                log.error("Can't parse certificate from JWK", clientJWK, e);
                return Optional.empty();
            }

            try {
                List<X509Certificate> certChain = jwk.getParsedX509CertChain();
                if (!certChain.isEmpty()) {
                    Psd2CertInfo certInfo = new Psd2CertInfo(certChain);
                    if (certInfo.isPsd2Cert()) {
                        log.debug("Client has presented psd2 certificate: {}", certInfo);
                        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                        List<GrantedAuthority> authoritiesList = new LinkedList<>();
                        for(GrantedAuthority authority : authorities){
                            authoritiesList.add(authority);
                        }
                        UserContext.UserType userType = UserContext.UserType.EIDAS;
                        if (userDetails.getUserType() == UserContext.UserType.OIDC_CLIENT) {
                            userType = UserContext.UserType.OIDC_CLIENT;
                        }
                        return Optional.of(UserContext.create(userDetails.getUsername(), authoritiesList,
                                userType, certInfo, certChain.toArray(new X509Certificate[0])));
                    }
                }
            } catch ( InvalidPsd2EidasCertificate e){
                log.warn("Failed to obtain Psd2 Info", e);
                return Optional.of(userDetails);
            }

            return Optional.of(userDetails);

        }
        return Optional.empty();
    }

    private Optional<UserContext> isExternalForgeRockAppFromCert(X509Certificate[] certificatesChain) {
        try {
            X509Certificate certificate = (X509Certificate) keyStoreService.getKeyStore().getCertificate(matlsConfigurationProperties.getForgerockExternalCAAlias());

            if (certificatesChain.length > 1 && certificate.equals(certificatesChain[1])) {
                try {
                    return Optional.of(getUserDetails(JWK.parse(certificatesChain[0]), certificatesChain));

                } catch (JOSEException e) {
                    log.error("Can't parse jwk from certificate '{}'", certificatesChain[0]);
                    return Optional.empty();
                }
            }
        } catch (KeyStoreException e) {
            log.error("Can't get ForgeRock external CA", e);
        }
        return Optional.empty();
    }

    protected UserContext getUserDetails(JWK jwk, X509Certificate[] certificatesChain) {
        ApplicationIdentity authenticate = directoryService.authenticate(jwk);
        List<GrantedAuthority> authorities = new ArrayList<>(authenticate.getRoles());
        return UserContext.create(authenticate.getId(), authorities, UserContext.UserType.JWKMS_APPLICATION, certificatesChain);
    }
}

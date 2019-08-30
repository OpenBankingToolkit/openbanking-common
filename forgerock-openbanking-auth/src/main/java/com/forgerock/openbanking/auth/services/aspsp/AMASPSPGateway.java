/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.aspsp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AMASPSPGateway extends AMGateway {

    public AMASPSPGateway(RestTemplate restTemplate,
                          @Value("${am.internal.root}") String amRoot,
                          @Value("${am.hostname}") String hostname,
                          @Value("${scgw.port}") String scgwPort,
                          @Value("${am.trusted-header-certificate}") String trustedHeaderCertificate
    ) {
        super(restTemplate, amRoot, hostname, trustedHeaderCertificate);
    }
}

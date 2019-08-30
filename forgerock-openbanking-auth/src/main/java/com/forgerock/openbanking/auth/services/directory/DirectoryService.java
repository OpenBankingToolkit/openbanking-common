/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.directory;

import com.forgerock.openbanking.auth.config.DirectoryConfiguration;
import com.forgerock.openbanking.auth.model.ApplicationIdentity;
import com.forgerock.openbanking.auth.model.SoftwareStatement;
import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Access the Jwk MS services
 */
@Service
@Slf4j
public class DirectoryService {

    private DirectoryConfiguration directoryConfiguration;
    private RestTemplate restTemplate;

    @Autowired
    public DirectoryService(DirectoryConfiguration directoryConfiguration, RestTemplate restTemplate) {
        this.directoryConfiguration = directoryConfiguration;
        this.restTemplate = restTemplate;
    }

    public ApplicationIdentity authenticate(JWK jwk) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParameterizedTypeReference<ApplicationIdentity> ptr = new ParameterizedTypeReference<ApplicationIdentity>() {};
        HttpEntity<String> request = new HttpEntity<>(jwk.toJSONObject().toJSONString(), headers);

        ResponseEntity<ApplicationIdentity> entity = restTemplate.exchange(directoryConfiguration.authenticateEndpoint,
                HttpMethod.POST, request, ptr);

        return entity.getBody();
    }

    public List<SoftwareStatement> getSoftwareStatements(String organisationId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParameterizedTypeReference<List<SoftwareStatement>> ptr = new ParameterizedTypeReference<List<SoftwareStatement>>() {};
        HttpEntity<String> request = new HttpEntity<>(headers);

       return restTemplate.exchange(directoryConfiguration.rootEndpoint + "/api/organisation/" + organisationId + "/software-statements",
                HttpMethod.GET, request, ptr).getBody();
    }


    public SoftwareStatement getCurrentSoftwareStatement() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParameterizedTypeReference<SoftwareStatement> ptr = new ParameterizedTypeReference<SoftwareStatement>() {};
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(directoryConfiguration.rootEndpoint + "/api/software-statement/current",
                HttpMethod.GET, request, ptr).getBody();
    }

    public SoftwareStatement createSoftwareStatement(SoftwareStatement softwareStatement) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        ParameterizedTypeReference<SoftwareStatement> ptr = new ParameterizedTypeReference<SoftwareStatement>() {};
        HttpEntity<SoftwareStatement> request = new HttpEntity<>(softwareStatement, headers);

        return restTemplate.exchange(directoryConfiguration.rootEndpoint + "/api/software-statement/",
                HttpMethod.POST, request, ptr).getBody();
    }

    public SoftwareStatement updateSoftwareStatement(SoftwareStatement softwareStatement) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        ParameterizedTypeReference<SoftwareStatement> ptr = new ParameterizedTypeReference<SoftwareStatement>() {};
        HttpEntity<SoftwareStatement> request = new HttpEntity<>(softwareStatement, headers);

        return restTemplate.exchange(directoryConfiguration.rootEndpoint + "/api/software-statement/" + softwareStatement.getId(),
                HttpMethod.PUT, request, ptr).getBody();
    }

    public String generateSSA(SoftwareStatement softwareStatement) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {};
        HttpEntity request = new HttpEntity<>(headers);
        return restTemplate.exchange(directoryConfiguration.rootEndpoint + "/api/software-statement/"
                        + softwareStatement.getId() + "/ssa",
                HttpMethod.POST, request, ptr).getBody();
    }

    public boolean deleteSoftwareStatement(String softwareStatementId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        ParameterizedTypeReference<SoftwareStatement> ptr = new ParameterizedTypeReference<SoftwareStatement>() {};
        HttpEntity<SoftwareStatement> request = new HttpEntity<>(headers);

        restTemplate.exchange(directoryConfiguration.rootEndpoint + "/api/software-statement/" + softwareStatementId,
                HttpMethod.DELETE, request, ptr);
        return true;
    }

    public String getCurrentTransportPem(String softwareStatementId, String kid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {};
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(directoryConfiguration.rootEndpoint + "/api/software-statement/" + softwareStatementId
                        + "/application/" + kid + "/download/publicCert",
                HttpMethod.GET, request, ptr).getBody();
    }
}

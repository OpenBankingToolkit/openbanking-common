/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.am;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UserProfileService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    @Resource(name = "forExternalForgeRockApplication")
    private RestTemplate restTemplate;

    public Map<String, String> getProfile(String ssoToken, String userProfileEndpoint, String cookieName) {
        LOGGER.info("Get user profile behind the sso token");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", cookieName + "=" + ssoToken);
        headers.add("Accept-API-Version", "protocol=1.0,resource=1.0");
        //Send request
        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);
        LOGGER.debug("Send user info request to the AS {}", userProfileEndpoint);

        return restTemplate.postForObject(userProfileEndpoint, request, Map.class);
    }
}

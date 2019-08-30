/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.oidc;

import com.forgerock.openbanking.auth.model.oidc.UserInfoResponse;
import com.forgerock.openbanking.auth.services.aspsp.AMAuthGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.org.openbanking.OBHeaders;

@Service
public class UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoService.class);

    @Autowired
    private AMAuthGateway amAuthGateway;

    public UserInfoResponse getUserInfo(RestTemplate restTemplate, String userInfoEndpoint,
                                        String accessToken) {
        LOGGER.info("Call user info '{}' with access token '{}'", userInfoEndpoint, accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(OBHeaders.AUTHORIZATION, "Bearer " + accessToken);
        ParameterizedTypeReference<UserInfoResponse> ptr = new ParameterizedTypeReference<UserInfoResponse>() {};

        return (UserInfoResponse) amAuthGateway.toAM(userInfoEndpoint, HttpMethod.GET, headers, ptr, null).getBody();
    }
}

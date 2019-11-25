/**
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

import com.forgerock.openbanking.am.gateway.AMAuthGateway;
import com.forgerock.openbanking.model.oidc.UserInfoResponse;
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

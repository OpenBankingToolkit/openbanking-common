/**
 * Copyright 2019 ForgeRock AS.
 *
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
package com.forgerock.openbanking.am.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
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

/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.ssl.services;

import com.forgerock.openbanking.ssl.model.ForgeRockApplicationResponse;
import org.springframework.web.client.RestTemplate;

public interface ForgeRockApplicationService {

     ForgeRockApplicationResponse getCurrentApplication(RestTemplate restTemplate);
}

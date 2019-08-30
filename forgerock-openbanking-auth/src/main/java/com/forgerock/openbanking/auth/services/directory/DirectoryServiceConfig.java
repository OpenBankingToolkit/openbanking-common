/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.directory;

import com.forgerock.openbanking.auth.config.DirectoryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configuration
public class DirectoryServiceConfig {

    @Autowired
    private DirectoryConfiguration directoryConfiguration;
    @Autowired
    private RestTemplate restTemplate;
    @Resource(name = "forExternal")
    private RestTemplate restTemplateExternal;


    @Bean(name="directoryServiceAsTpp")
    public DirectoryService directoryServiceAsTpp() {
        return new DirectoryService(directoryConfiguration, restTemplateExternal);
    }

    @Bean(name="directoryServiceAsForgeRockApp")
    public DirectoryService directoryServiceAsForgeRockApp() {
        return new DirectoryService(directoryConfiguration, restTemplate);
    }
}

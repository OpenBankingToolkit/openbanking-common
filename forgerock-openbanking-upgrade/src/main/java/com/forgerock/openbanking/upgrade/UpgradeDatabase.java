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
package com.forgerock.openbanking.upgrade;

import com.forgerock.openbanking.upgrade.model.version.EntitiesVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
public class UpgradeDatabase extends UpgradeApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeDatabase.class);

    @Autowired
    private EntitiesVersionRepository entitiesVersionRepository;

    @Bean
    public CommandLineRunner upgrade() {
        return this;
    }

    @Override
    public EntitiesVersion getStoredVersion() {
        List<EntitiesVersion> entitiesVersions = cleanDuplicatedVersions(entitiesVersionRepository.findAll());
        EntitiesVersion entitiesVersion;
        if (entitiesVersions.isEmpty()) {
            entitiesVersion = new EntitiesVersion();
            entitiesVersion.setStatus(EntitiesVersion.UpgradeStatus.UPGRADED);
            entitiesVersion.setVersion("0");
        } else if (entitiesVersions.size() > 1) {
            Optional<EntitiesVersion> versionOptional = entitiesVersionRepository.findByVersionAndStatus(
                    buildProperties.getVersion(),
                    EntitiesVersion.UpgradeStatus.UPGRADED.name()
            );
            if (versionOptional.isPresent()) {
                LOGGER.info("Current {} version {} found", EntitiesVersion.UpgradeStatus.UPGRADED.name(), buildProperties.getVersion());
                return versionOptional.get();
            }
            LOGGER.error("More than one version found: " + entitiesVersions);
            throw new RuntimeException("More than one version found: " + entitiesVersions);
        } else {
            entitiesVersion = entitiesVersions.get(0);
        }
        return entitiesVersion;
    }

    @Override
    public EntitiesVersion saveEntitiesVersion(EntitiesVersion entitiesVersion) {
        return entitiesVersionRepository.save(entitiesVersion);
    }

    @Override
    public void deleteEntitiesVersion(EntitiesVersion entitiesVersion) {
        LOGGER.debug("Deleting duplicated version \n{}", entitiesVersion.toString());
        entitiesVersionRepository.delete(entitiesVersion);
    }
}

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
package com.forgerock.openbanking.upgrade;

import com.forgerock.openbanking.upgrade.model.Version;
import com.forgerock.openbanking.upgrade.model.version.EntitiesVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.annotation.Order;

@Order(2)
public abstract class UpgradeApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeApplication.class);

    @Autowired
    private UpgradeStepsRegistry upgradeStepsRegistry;
    @Autowired
    BuildProperties buildProperties;

    /**
     * Force upgrade any upgrade steps that have the same version as the current version
     **/
    @Value("${upgrade.force:false}")
    private boolean forceUpgrade;

    @Override
    public void run(String... args) {
        String version = getVersion();
        LOGGER.debug("Version found from the property file : " + version);
        Version to = new Version(version);

        EntitiesVersion entitiesVersion = getPreviousVersion();
        LOGGER.debug("Version found from the current system : " + entitiesVersion);
        LOGGER.debug("forceUpgrade : " + forceUpgrade);

        if (!forceUpgrade &&  entitiesVersion.getStatus() == EntitiesVersion.UpgradeStatus.IN_PROCESS) {
            LOGGER.info("Database under upgrade already");
            return;
        }
        Version from = new Version(entitiesVersion.getVersion());

        if (!forceUpgrade && from.equals(to)) {
            LOGGER.info("Already upgraded.");
            return;
        }
        LOGGER.info("Start upgrading");
        entitiesVersion.setStatus(EntitiesVersion.UpgradeStatus.IN_PROCESS);
        entitiesVersion = saveEntitiesVersion(entitiesVersion);

        try {
            if (upgradeStepsRegistry.callUpgradeSteps(from, to)) {
                entitiesVersion.setVersion(to.version);
                LOGGER.info("Upgrading done");
            }
        } finally {
            LOGGER.info("Upgrading failed or succeed, revert back the version option as upgraded without changing the version number");
            entitiesVersion.setStatus(EntitiesVersion.UpgradeStatus.UPGRADED);
            saveEntitiesVersion(entitiesVersion);
        }
    }

    public abstract EntitiesVersion getPreviousVersion();

    public abstract EntitiesVersion saveEntitiesVersion(EntitiesVersion entitiesVersion);

    private String getVersion() {
        return buildProperties.getVersion();
    }

}

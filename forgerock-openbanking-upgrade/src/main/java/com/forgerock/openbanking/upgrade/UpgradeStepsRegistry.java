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

import com.forgerock.openbanking.upgrade.exceptions.UpgradeException;
import com.forgerock.openbanking.upgrade.model.UpgradeMeta;
import com.forgerock.openbanking.upgrade.model.UpgradeStep;
import com.forgerock.openbanking.upgrade.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class UpgradeStepsRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeStepsRegistry.class);

    @Autowired
    private ApplicationContext applicationContext;

    public TreeMap<Version, UpgradeStep> getUpgradeSteps() {
        Map<String, UpgradeStep> beansOfType = applicationContext.getBeansOfType(UpgradeStep.class);

        TreeMap<Version, UpgradeStep> upgradeSteps = new TreeMap<>();
        for (Map.Entry<String, UpgradeStep> entry : beansOfType.entrySet()){
            UpgradeMeta upgradeMeta = entry.getValue().getClass().getDeclaredAnnotation(UpgradeMeta.class);
            if (upgradeMeta != null) {
                upgradeSteps.put(new Version(upgradeMeta.version()), entry.getValue());
            }
        }
        return upgradeSteps;
    }

    public boolean callUpgradeSteps(Version versionFrom, Version versionTo) {
        LOGGER.debug("Upgrade from {} to {} ", versionFrom, versionTo);

        try {
            for (Map.Entry<Version, UpgradeStep> upgradeStep : getUpgradeSteps().entrySet()) {
                LOGGER.debug("Check if we need to upgrade to {}", upgradeStep.getKey().version);
                if (upgradeStep.getKey().compareTo(versionFrom) >= 0 && upgradeStep.getKey().compareTo(versionTo) <= 0) {
                    LOGGER.debug("Yes, executing the upgrade step {}", upgradeStep.getKey().version);
                    upgradeStep.getValue().upgrade();
                    LOGGER.debug("Upgrade {} executed", upgradeStep.getKey().version);
                } else {
                    LOGGER.debug("No, skipping the upgrade step {}", upgradeStep.getKey().version);
                }
            }
            LOGGER.debug("All upgrade steps done.");
            return true;
        } catch (UpgradeException e) {
            LOGGER.error("Could not upgrade", e);
            return false;
        }
    }
}

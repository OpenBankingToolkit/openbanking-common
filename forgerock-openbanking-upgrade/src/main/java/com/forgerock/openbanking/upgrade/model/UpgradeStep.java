/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.upgrade.model;

import com.forgerock.openbanking.upgrade.exceptions.UpgradeException;

public interface UpgradeStep {

    boolean upgrade() throws UpgradeException;
}

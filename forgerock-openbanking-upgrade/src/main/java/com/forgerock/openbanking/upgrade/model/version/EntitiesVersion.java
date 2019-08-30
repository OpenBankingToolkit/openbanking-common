/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.upgrade.model.version;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class EntitiesVersion {

    @Id
    public String id;
    public String version;
    public UpgradeStatus status;
    @LastModifiedDate
    public Date updated;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public UpgradeStatus getStatus() {
        return status;
    }

    public void setStatus(UpgradeStatus status) {
        this.status = status;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public enum UpgradeStatus {
        UPGRADED, IN_PROCESS
    }

    @Override
    public String toString() {
        return "EntitiesVersion{" +
                "version='" + version + '\'' +
                ", status=" + status +
                ", updated=" + updated +
                '}';
    }
}

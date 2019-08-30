/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserGroup implements GrantedAuthority {

    GROUP_FORGEROCK("forgerock"),
    GROUP_OB("openbanking"),

    ;

    private String groupeId;

    UserGroup(String groupeId) {
        this.groupeId = groupeId;
    }

    public String getAuthority() {
        return this.name();
    }

    public static UserGroup valueFromGroupeId(String groupeId) {
        for (UserGroup userGroup: UserGroup.values()) {
            if (userGroup.groupeId.equals(groupeId)) {
                return userGroup;
            }
        }
        return null;
    }
}

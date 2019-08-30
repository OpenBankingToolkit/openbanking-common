/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model;

import org.springframework.security.core.GrantedAuthority;

public enum OBRIAuthorities implements GrantedAuthority {

    DATA("data", OBRIRole.ROLE_DATA),
    ;

    private String id;
    private OBRIRole role;

    OBRIAuthorities(String id, OBRIRole role) {
        this.id = id;
        this.role = role;
    }

    public String getAuthority() {
        return this.name();
    }

    public OBRIRole getRole() {
        return role;
    }

    public static OBRIAuthorities valueFromId(String id) {
        for (OBRIAuthorities userGroup: OBRIAuthorities.values()) {
            if (userGroup.id.equals(id)) {
                return userGroup;
            }
        }
        return null;
    }
}

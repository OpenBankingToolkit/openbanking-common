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
package com.forgerock.openbanking.model;

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

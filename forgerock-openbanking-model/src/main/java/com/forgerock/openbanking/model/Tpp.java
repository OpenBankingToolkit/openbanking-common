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
package com.forgerock.openbanking.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Tpp {
    @Id
    @Indexed
    public String id;
    public String directoryId;
    public String name;
    public String officialName;
    @Indexed
    private String certificateCn;
    @Indexed
    private String softwareId;
    @Indexed
    private String organisationId;
    @Indexed
    private String clientId;
    private DirectorySoftwareStatement ssa;
    private String tppRequest;
    private OIDCRegistrationResponse registrationResponse;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    private Set<SoftwareStatementRole> types = new HashSet<>();

    public DirectorySoftwareStatement getSsaClaim() {
        return ssa;
    }

    public String getLogo() {
        if (getSsa() != null) {
            return ssa.getSoftware_logo_uri();
        }
        return null;
    }

    public Optional<String> getRegistrationAccessToken(){
        if(registrationResponse != null){
            String registationAccessToken = registrationResponse.getRegistrationAccessToken();
            if(registationAccessToken != null && !registationAccessToken.isBlank()) {
                return Optional.of(registationAccessToken);
            }
        }
        return Optional.empty();
    }
}

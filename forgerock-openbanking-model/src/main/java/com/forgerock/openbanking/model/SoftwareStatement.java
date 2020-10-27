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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document
public class SoftwareStatement {
    @Id
    private String id;
    private String name;
    private String description;
    private String uri;
    private String version;
    private String environment;
    private String logoUri;
    private Mode mode;
    private Set<SoftwareStatementRole> roles;
    private Status status;
    private String policyUri;
    private String termsOfService;
    private List<String> redirectUris;
    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;
    private String applicationId;

    /**
     * The software ID is the same as the ForgeRock Application Id. It is included here to make logic simpler for
     * TPPS wishing to support message signing with both ForgeRock certificates and OB issued certificates. OB issued
     * Software Statements contain software_id and org_id fields and specify that these values should be used in the
     * iss field of a jws header (message signing header). Having both the software_id and org_id fields in the
     * ForgeRock software statement will make TPP and our own test logic easier when performing message signing.
     */
    @JsonProperty("software_id")
    private String softwareId;
    /**
     * The organisation ID - this is the organisation in the ForgeRock directory under which this
     * application/software id has been created.
     */
    @JsonProperty("org_id")
    private String organisationId;

    /**
     * Default constructor. Sets the initial status and mode.
     */
    public SoftwareStatement() {
        status = Status.ACTIVE;

        roles = new HashSet<>();
        roles.add(SoftwareStatementRole.AISP);
        roles.add(SoftwareStatementRole.PISP);
        roles.add(SoftwareStatementRole.CBPII);
        roles.add(SoftwareStatementRole.DATA);

        redirectUris = new ArrayList<>();
        mode = Mode.TEST;
    }

    public enum Mode {
        TEST, LIVE
    }

    public enum Status {
        ACTIVE, REVOKED
    }
}

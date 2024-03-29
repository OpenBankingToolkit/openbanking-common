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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "iss")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectorySoftwareStatementOpenBanking.class, name = "ForgeRock"),
        @JsonSubTypes.Type(value = DirectorySoftwareStatementOpenBanking.class, name = "OpenBanking Ltd")})
public interface DirectorySoftwareStatement {

    String getJti();

    String getSoftware_jwks_endpoint();

    List<String> getSoftware_redirect_uris();

    String getIss();

    String getSoftware_client_id();

    String getSoftware_id();

    String getOrg_name();

    String getOrg_id();

    String getSoftware_logo_uri();

    String getSoftware_tos_uri();

    String getSoftware_policy_uri();

    List<String> getSoftware_roles();

    boolean hasRole(SoftwareStatementRole role);

    List<OrganisationContact> getOrg_contacts();

    String getAuthorisationNumber();
}

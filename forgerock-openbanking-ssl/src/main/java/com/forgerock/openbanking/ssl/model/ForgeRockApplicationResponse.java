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
package com.forgerock.openbanking.ssl.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.forgerock.openbanking.serialiser.nimbus.JWKDeserializer;
import com.forgerock.openbanking.serialiser.nimbus.JWKSerializer;
import com.nimbusds.jose.jwk.JWK;

public class ForgeRockApplicationResponse {

    private String applicationId;

    @JsonDeserialize(using = JWKDeserializer.class)
    @JsonSerialize(using = JWKSerializer.class)
    private JWK transportKey;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public JWK getTransportKey() {
        return transportKey;
    }

    public void setTransportKey(JWK transportKey) {
        this.transportKey = transportKey;
    }
}

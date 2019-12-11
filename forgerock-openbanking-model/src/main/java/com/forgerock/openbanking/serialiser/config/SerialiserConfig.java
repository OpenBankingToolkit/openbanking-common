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
package com.forgerock.openbanking.serialiser.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.forgerock.openbanking.serialiser.*;
import com.forgerock.openbanking.serialiser.nimbus.*;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import org.joda.time.DateTime;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

@Configuration
public class SerialiserConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(Base64.class, new Base64Serialiser());
            jacksonObjectMapperBuilder.serializerByType(DateTime.class, new IsoDateTimeSerializer());
            jacksonObjectMapperBuilder.deserializerByType(DateTime.class, new IsoDateTimeDeserializer());
            jacksonObjectMapperBuilder.deserializerByType(OBExternalPermissions1Code.class, new OBExternalPermissions1CodeDeserializer());
            jacksonObjectMapperBuilder.deserializerByType(JWTClaimsSet.class, new JWTClaimsSetDeserializer());
            jacksonObjectMapperBuilder.serializerByType(OBExternalPermissions1Code.class, new OBExternalPermissions1CodeSerializer());
            jacksonObjectMapperBuilder.serializerByType(HttpTrace.class, new HttpTraceSerialiser());
            jacksonObjectMapperBuilder.serializerByType(JWTClaimsSet.class, new JWTClaimsSetSerializer());

            jacksonObjectMapperBuilder.serializerByType(JWK.class, new JWKSerializer());
            jacksonObjectMapperBuilder.deserializerByType(JWK.class, new JWKDeserializer());

            jacksonObjectMapperBuilder.serializerByType(JWKSet.class, new JWKSetSerializer());
            jacksonObjectMapperBuilder.deserializerByType(JWKSet.class, new JWKSetDeserializer());

            jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
        };
    }
}

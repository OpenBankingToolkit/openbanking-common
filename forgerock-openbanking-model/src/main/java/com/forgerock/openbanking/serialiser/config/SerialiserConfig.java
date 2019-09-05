/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.serialiser.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.forgerock.openbanking.serialiser.Base64Serialiser;
import com.forgerock.openbanking.serialiser.IsoDateTimeDeserializer;
import com.forgerock.openbanking.serialiser.IsoDateTimeSerializer;
import com.forgerock.openbanking.serialiser.nimbus.*;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerialiserConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(Base64.class, new Base64Serialiser());
            jacksonObjectMapperBuilder.serializerByType(DateTime.class, new IsoDateTimeSerializer());
            jacksonObjectMapperBuilder.deserializerByType(DateTime.class, new IsoDateTimeDeserializer());
            jacksonObjectMapperBuilder.deserializerByType(JWTClaimsSet.class, new JWTClaimsSetDeserializer());
            jacksonObjectMapperBuilder.serializerByType(JWTClaimsSet.class, new JWTClaimsSetSerializer());

            jacksonObjectMapperBuilder.serializerByType(JWK.class, new JWKSerializer());
            jacksonObjectMapperBuilder.deserializerByType(JWK.class, new JWKDeserializer());

            jacksonObjectMapperBuilder.serializerByType(JWKSet.class, new JWKSetSerializer());
            jacksonObjectMapperBuilder.deserializerByType(JWKSet.class, new JWKSetDeserializer());

            jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
        };
    }
}

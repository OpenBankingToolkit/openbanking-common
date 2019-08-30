/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.serialiser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.forgerock.openbanking.auth.model.error.OBRIErrorType;

import java.io.IOException;

public class OBRIErrorTypeSerializer extends JsonSerializer<OBRIErrorType> {
    @Override
    public void serialize(
            OBRIErrorType errorType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("Name", errorType.name());
        jsonGenerator.writeStringField("HttpStatus", errorType.getHttpStatus().name());
        jsonGenerator.writeStringField("Code", errorType.getCode().getValue());
        jsonGenerator.writeStringField("Message", errorType.getMessage());
        if (errorType.getPath() != null) {
            jsonGenerator.writeStringField("Path", errorType.getPath());
        }
        jsonGenerator.writeEndObject();
    }
}

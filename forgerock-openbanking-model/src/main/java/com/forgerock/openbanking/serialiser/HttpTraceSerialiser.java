/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.serialiser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.actuate.trace.http.HttpTrace;

import java.io.IOException;

public class HttpTraceSerialiser extends StdSerializer<HttpTrace> {

    public HttpTraceSerialiser() {
        this(null);
    }

    public HttpTraceSerialiser(Class<HttpTrace> t) {
        super(t);
    }

    @Override
    public void serialize(HttpTrace httpTrace, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("timeTaken", httpTrace.getTimeTaken());
        jsonGenerator.writeNumberField("timestamp", httpTrace.getTimestamp().toEpochMilli());
        jsonGenerator.writeObjectField("principal", httpTrace.getPrincipal());
        jsonGenerator.writeObjectField("request", httpTrace.getRequest());
        jsonGenerator.writeObjectField("response", httpTrace.getResponse());
        jsonGenerator.writeObjectField("session", httpTrace.getSession());
        jsonGenerator.writeEndObject();
    }
}

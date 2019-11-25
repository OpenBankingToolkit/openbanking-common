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

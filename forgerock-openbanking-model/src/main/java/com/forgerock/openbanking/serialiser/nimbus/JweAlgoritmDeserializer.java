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
package com.forgerock.openbanking.serialiser.nimbus;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nimbusds.jose.JWEAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JweAlgoritmDeserializer extends StdDeserializer<JWEAlgorithm> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JweAlgoritmDeserializer.class);

    public JweAlgoritmDeserializer() {
        this(null);
    }

    public JweAlgoritmDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public JWEAlgorithm deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return JWEAlgorithm.parse(jsonParser.getText());
    }
}

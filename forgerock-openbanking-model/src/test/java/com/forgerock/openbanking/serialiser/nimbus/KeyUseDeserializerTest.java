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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.serialiser.nimbus.KeyUseDeserializer;
import com.nimbusds.jose.jwk.KeyUse;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class KeyUseDeserializerTest {

    @Test
    public void testSerialisation() throws IOException {
        // Given
        JsonParser jsonParser = new JsonFactory().createParser("\"sig\"");
        jsonParser.setCodec(new ObjectMapper());

        // When
        KeyUse keyUse = new KeyUseDeserializer().deserialize(jsonParser, null);

        // Then
        assertEquals(keyUse, KeyUse.SIGNATURE);
    }

}
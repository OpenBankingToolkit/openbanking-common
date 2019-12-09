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
package com.forgerock.openbanking.serialiser.nimbus;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.forgerock.openbanking.serialiser.nimbus.KeyUseSerializer;
import com.nimbusds.jose.jwk.KeyUse;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class KeyUseSerializerTest {

    @Test
    public void testSerialisation() throws IOException {
        // Given
        KeyUse signature = KeyUse.SIGNATURE;
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonFactory().createGenerator(s);

        // When
        new KeyUseSerializer().serialize(signature, generator, null);
        generator.flush();

        // Then
        assertEquals(s.toString(), "\"sig\"");
    }
}
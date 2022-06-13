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
package com.forgerock.openbanking.serialiser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class IsoLocalDateDeserializerTest {
    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;

    @Test
    public void testDeserialize() throws IOException {
        BDDMockito.given(jsonParser.getText()).willReturn("2022-06-10");
        final IsoLocalDateDeserializer deserializer = new IsoLocalDateDeserializer();

        LocalDate date = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(new LocalDate(2022, 6, 10), date);
    }
}

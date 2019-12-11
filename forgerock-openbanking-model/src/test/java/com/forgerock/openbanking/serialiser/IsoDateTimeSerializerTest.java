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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IsoDateTimeSerializerTest {
    @Mock
    private JsonGenerator jsonGenerator;
    @Mock
    private SerializerProvider serializerProvider;

    @Test
    public void testSerialisationParis() throws IOException {

        // Given
        IsoDateTimeSerializer isoDateTimeSerializer = new IsoDateTimeSerializer();
        DateTime dateTimeParis = new DateTime(2000, 1, 2, 3, 4, 5, 6,
                DateTimeZone.forID("Europe/Paris"));

        // When
        isoDateTimeSerializer.serialize(dateTimeParis, jsonGenerator, serializerProvider);

        //Then
        Mockito.verify(jsonGenerator).writeObject("2000-01-02T03:04:05.006+01:00");
    }

    @Test
    public void testSerialisationUTC() throws IOException {

        // Given
        IsoDateTimeSerializer isoDateTimeSerializer = new IsoDateTimeSerializer();
        DateTime dateTimeUTC = new DateTime(2000, 1, 2, 3, 4, 5, 6,
                DateTimeZone.UTC);

        // When
        isoDateTimeSerializer.serialize(dateTimeUTC, jsonGenerator, serializerProvider);

        //Then
        Mockito.verify(jsonGenerator).writeObject("2000-01-02T03:04:05.006Z");
    }

}
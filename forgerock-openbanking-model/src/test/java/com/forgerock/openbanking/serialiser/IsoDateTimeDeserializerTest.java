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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class IsoDateTimeDeserializerTest {
    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;

    @Test
    public void testDeserializerUTC() throws IOException {

        // Given
        IsoDateTimeDeserializer isoDateTimeDeserializer = new IsoDateTimeDeserializer();
        BDDMockito.given(jsonParser.getText()).willReturn("2000-01-02T03:04:05.006Z");

        // When
        DateTime resultingDateTime = isoDateTimeDeserializer.deserialize(jsonParser, deserializationContext);
        
        //Then
        DateTime dateTimeUTC = new DateTime(2000, 1, 2, 3, 4, 5, 6,
                DateTimeZone.UTC);
        Assertions.assertThat(resultingDateTime).isEqualByComparingTo(dateTimeUTC);
    }

    @Test
    public void testDeserializerUTCWithTimezone() throws IOException {

        // Given
        IsoDateTimeDeserializer isoDateTimeDeserializer = new IsoDateTimeDeserializer();
        BDDMockito.given(jsonParser.getText()).willReturn("2000-01-02T03:04:05.006+00:00");

        // When
        DateTime resultingDateTime = isoDateTimeDeserializer.deserialize(jsonParser, deserializationContext);

        //Then
        DateTime dateTimeUTC = new DateTime(2000, 1, 2, 3, 4, 5, 6,
                DateTimeZone.UTC);
        Assertions.assertThat(resultingDateTime).isEqualByComparingTo(dateTimeUTC);
    }

    @Test
    public void testDeserializerUTCWithTimezoneParis() throws IOException {

        // Given
        IsoDateTimeDeserializer isoDateTimeDeserializer = new IsoDateTimeDeserializer();
        BDDMockito.given(jsonParser.getText()).willReturn("2000-01-02T03:04:05.006+01:00");

        // When
        DateTime resultingDateTime = isoDateTimeDeserializer.deserialize(jsonParser, deserializationContext);

        //Then
        DateTime dateTimeUTC = new DateTime(2000, 1, 2, 3, 4, 5, 6,
                DateTimeZone.forID("Europe/Paris"));
        Assertions.assertThat(resultingDateTime).isEqualByComparingTo(dateTimeUTC);
    }

    @Test
    public void testDeserializerUTCWithTimezoneNoMillis() throws IOException {

        // Given
        IsoDateTimeDeserializer isoDateTimeDeserializer = new IsoDateTimeDeserializer();
        BDDMockito.given(jsonParser.getText()).willReturn("2000-01-02T03:04:05+00:00");

        // When
        DateTime resultingDateTime = isoDateTimeDeserializer.deserialize(jsonParser, deserializationContext);

        //Then
        DateTime dateTimeUTC = new DateTime(2000, 1, 2, 3, 4, 5, 0,
                DateTimeZone.UTC);
        Assertions.assertThat(resultingDateTime).isEqualByComparingTo(dateTimeUTC);
    }

    @Test
    public void testDeserializerUTCWithNanoSeconds() throws IOException {

        // Given
        IsoDateTimeDeserializer isoDateTimeDeserializer = new IsoDateTimeDeserializer();
        BDDMockito.given(jsonParser.getText()).willReturn("2000-01-02T03:04:05.601822Z");

        // When
        DateTime resultingDateTime = isoDateTimeDeserializer.deserialize(jsonParser, deserializationContext);

        //Then
        DateTime dateTimeUTC = new DateTime(2000, 1, 2, 3, 4, 5, 601,
                DateTimeZone.UTC);
        Assertions.assertThat(resultingDateTime).isEqualByComparingTo(dateTimeUTC);
    }

}
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
package com.forgerock.openbanking.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;

@Slf4j
public class DirectorySoftwareStatementTest extends TestCase {

    final private ObjectMapper mapper = new ObjectMapper();

    public void setUp() throws Exception {
        super.setUp();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void tearDown() throws Exception {
    }

    @Test
    public void testSerialisation() throws IOException {
        // Given
        InputStream ssa = getSsa("SSAs/ForgeRockDirectorySSA.json");
        DirectorySoftwareStatement value = mapper.readValue(ssa, DirectorySoftwareStatement.class);
        assertNotNull(value.getIss());
        assertNotNull(value);
        OutputStream os = new ByteArrayOutputStream();
        mapper.writeValue(os, value);
    }

    @Test
    public void testSerialisationOBIssuer() throws IOException {
        // Given
        InputStream ssa = getSsa("SSAs/OpenBankingAutomatedTestingSSA.json");

        Reader instream = new InputStreamReader(ssa);
        DirectorySoftwareStatement value = mapper.readValue(instream, DirectorySoftwareStatement.class);
        assertNotNull(value.getIss());
        OutputStream os = new ByteArrayOutputStream();
        mapper.writeValue(os, value);

        assertNotNull(value);
    }

    private InputStream getSsa(String path){
        return getClass().getClassLoader().getResourceAsStream(path);
    }
}
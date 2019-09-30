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
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.io.IOException;

public class OBExternalPermissions1CodeSerializer extends StdSerializer<OBExternalPermissions1Code> {

    public OBExternalPermissions1CodeSerializer() {
        this(null);
    }

    public OBExternalPermissions1CodeSerializer(Class<OBExternalPermissions1Code> t) {
        super(t);
    }

    @Override
    public void serialize(OBExternalPermissions1Code permissions1Code, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeObject(permissions1Code.toString());
    }
}

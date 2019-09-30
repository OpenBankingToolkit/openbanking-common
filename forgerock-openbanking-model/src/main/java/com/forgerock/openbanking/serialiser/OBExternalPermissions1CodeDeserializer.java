/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.serialiser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.io.IOException;

public class OBExternalPermissions1CodeDeserializer extends StdDeserializer<OBExternalPermissions1Code> {

    public OBExternalPermissions1CodeDeserializer() {
        this(null);
    }

    public OBExternalPermissions1CodeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public OBExternalPermissions1Code deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return OBExternalPermissions1Code.fromValue(jsonParser.getText());
    }
}

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
import com.nimbusds.jose.util.Base64;

import java.io.IOException;

public class Base64Serialiser extends StdSerializer<Base64> {

    public Base64Serialiser() {
        this(null);
    }

    public Base64Serialiser(Class<Base64> t) {
        super(t);
    }

    @Override
    public void serialize(Base64 base64, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeObject(base64.toString());
    }
}

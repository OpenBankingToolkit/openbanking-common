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
package com.forgerock.openbanking.constants;

import com.forgerock.openbanking.model.error.UnsupportedOIDCAuthMethodsException;
import com.forgerock.openbanking.model.error.UnsupportedOIDCGrantTypeException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class OIDCConstantsTest {

    @Test
    public void Should_get_GranType() {
        // when
        OIDCConstants.GrantType grantType = OIDCConstants.GrantType.fromType(OIDCConstants.GrantType.HEADLESS_AUTH.type);
        // then
        assertThat(grantType).isEqualTo(OIDCConstants.GrantType.HEADLESS_AUTH);
    }

    @Test
    public void Should_get_TokenEndpointAuthMethods() {
        // when
        OIDCConstants.TokenEndpointAuthMethods tokenEndpointAuthMethods = OIDCConstants.TokenEndpointAuthMethods.fromType(OIDCConstants.TokenEndpointAuthMethods.CLIENT_SECRET_BASIC.type);
        // then
        assertThat(tokenEndpointAuthMethods).isEqualTo(OIDCConstants.TokenEndpointAuthMethods.CLIENT_SECRET_BASIC);
    }

    @Test
    public void Should_get_GranType_unsupported() {
        // when
        String grantType = "wrong_grant_type";
        Exception error = catchThrowableOfType(
                () -> {
                    OIDCConstants.GrantType.fromType(grantType);
                },
                UnsupportedOIDCGrantTypeException.class
        );
        // then
        assertThat(error).isNotNull();
        assertThat(error).isInstanceOf(UnsupportedOIDCGrantTypeException.class);
        assertThat(error.getMessage()).isEqualTo("Type '" + grantType + "' doesn't match any of the grant types " + OIDCConstants.GrantType.getAllTypes() + " defined.");
    }

    @Test
    public void Should_get_TokenEndpointAuthMethods_unsupported() {
        // when
        String authMethod = "wrong_TokenEndpointAuthMethods";
        Exception error = catchThrowableOfType(
                () -> {
                    OIDCConstants.TokenEndpointAuthMethods.fromType(authMethod);
                },
                UnsupportedOIDCAuthMethodsException.class
        );
        // then
        assertThat(error).isNotNull();
        assertThat(error).isInstanceOf(UnsupportedOIDCAuthMethodsException.class);
        assertThat(error.getMessage()).isEqualTo("Type '" + authMethod + "' doesn't match any of the token endpoint auth methods " + OIDCConstants.TokenEndpointAuthMethods.getAllTypes());
    }
}

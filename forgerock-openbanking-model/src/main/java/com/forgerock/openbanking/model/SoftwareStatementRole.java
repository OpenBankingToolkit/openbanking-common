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
package com.forgerock.openbanking.model;


import com.forgerock.cert.psd2.Psd2Role;

import java.util.Optional;

public enum SoftwareStatementRole {
    // Account Information Service Provider
    AISP,
    // Payment Initiation Services Provider
    PISP,
    // Account Servicing Payment Service Provider
    ASPSP,
    // Card Based Payment Instrument Issuer
    CBPII,
    // A Role used to protect the /data endpoint
    DATA;

    public Optional<Psd2Role> getPsd2Role(){
        switch (this) {
            // Account Information Service Provider
            case AISP:
                return Optional.of(Psd2Role.PSP_AI);
            case PISP:
                return Optional.of(Psd2Role.PSP_PI);
            case ASPSP:
                return Optional.of(Psd2Role.PSP_AS);
            case CBPII:
                return Optional.of(Psd2Role.PSP_IC);
            case DATA:
                return Optional.empty();
            default:
                throw new IllegalArgumentException(this.toString() + " is an unrecognised role");
        }
    }

    public static Optional<SoftwareStatementRole> getSSRole(Psd2Role psd2Role){
        SoftwareStatementRole role = null;
        switch(psd2Role){
            case PSP_AI:
                role = SoftwareStatementRole.AISP;
                break;
            case PSP_PI:
                role = SoftwareStatementRole.PISP;
                break;
            case PSP_AS:
                role = SoftwareStatementRole.ASPSP;
                break;
            case PSP_IC:
                role = SoftwareStatementRole.CBPII;
                break;
        }
        return Optional.ofNullable(role);
    }
}
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
package com.forgerock.openbanking.ssl.model.csr;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public class CSRGenerationResponse {

    public String alias;
    public String csr;
    public PKCS10CertificationRequest pkcs10CertificationRequest;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    public PKCS10CertificationRequest getPkcs10CertificationRequest() {
        return pkcs10CertificationRequest;
    }

    public void setPkcs10CertificationRequest(PKCS10CertificationRequest pkcs10CertificationRequest) {
        this.pkcs10CertificationRequest = pkcs10CertificationRequest;
    }
}

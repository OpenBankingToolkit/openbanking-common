/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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

/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.ssl.model.csr;

public class CSRImportPemsRequest {

    public String encryptionPem;
    public String signingPem;

    public String getEncryptionPem() {
        return encryptionPem;
    }

    public void setEncryptionPem(String encryptionPem) {
        this.encryptionPem = encryptionPem;
    }

    public String getSigningPem() {
        return signingPem;
    }

    public void setSigningPem(String signingPem) {
        this.signingPem = signingPem;
    }
}

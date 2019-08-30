/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.exceptions;

import com.forgerock.openbanking.auth.model.error.OBRIErrorType;
import org.springframework.security.core.AuthenticationException;
import uk.org.openbanking.datamodel.error.OBError1;

public class OBErrorAuthenticationException extends AuthenticationException {

    private OBRIErrorType obriErrorType;
    private Object[] args;

    public OBErrorAuthenticationException(OBRIErrorType obriErrorType, Object... args) {
        super(String.format("%s: %s" , obriErrorType.toOBError1(args).getErrorCode(), obriErrorType.toOBError1(args).getMessage()));
        this.obriErrorType = obriErrorType;
        this.args = args;
    }

    public OBRIErrorType getObriErrorType() {
        return obriErrorType;
    }

    public OBError1 getOBError() {
        return obriErrorType.toOBError1(args);
    }
}

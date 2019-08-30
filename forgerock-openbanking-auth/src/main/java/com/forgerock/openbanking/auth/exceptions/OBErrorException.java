/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.exceptions;

import com.forgerock.openbanking.auth.model.error.OBRIErrorType;
import uk.org.openbanking.datamodel.error.OBError1;

public class OBErrorException extends Exception {

    private OBRIErrorType obriErrorType;
    private Object[] args;

    public OBErrorException(OBRIErrorType obriErrorType, Object... args) {
        super(String.format(obriErrorType.getMessage(), args));
        this.obriErrorType = obriErrorType;
        this.args = args;
    }

    public OBRIErrorType getObriErrorType() {
        return obriErrorType;
    }

    public Object[] getArgs() {
        return args;
    }

    public OBError1 getOBError() {
        return obriErrorType.toOBError1(args);
    }
}

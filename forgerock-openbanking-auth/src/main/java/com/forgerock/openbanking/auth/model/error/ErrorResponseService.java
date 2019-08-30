/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.error;

import brave.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.error.OBError1;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;

import java.util.List;

@Service
public class ErrorResponseService {
    @Autowired
    private Tracer tracer;

    public OBErrorResponse1 toOBErrorResponse1(String code, String message, List<OBError1> errors) {
        return new OBErrorResponse1()
                .code(code)
                .id(String.valueOf(tracer.currentSpan().context().traceId()))
                .message(message)
                .errors(errors);
    }
}

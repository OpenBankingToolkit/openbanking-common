/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.exceptions;

import com.forgerock.openbanking.auth.model.error.ErrorResponseTmp;
import com.forgerock.openbanking.auth.model.error.OBRIErrorResponseCategory;
import org.springframework.http.HttpStatus;
import uk.org.openbanking.datamodel.error.OBError1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OBErrorResponseException extends Exception {

    private HttpStatus status;
    private ErrorResponseTmp errorResponseTmp;
    private List<OBError1> errors = new ArrayList<>();
    private OBRIErrorResponseCategory category;
    private String id;

    public OBErrorResponseException(HttpStatus status, OBRIErrorResponseCategory category, List<OBError1> errors) {
        this.status = status;
        this.category = category;
        this.errors = errors;
    }

    public OBErrorResponseException(HttpStatus status, OBRIErrorResponseCategory category, String id, List<OBError1> errors) {
        this.status = status;
        this.category = category;
        this.id = id;
        this.errors = errors;
    }

    public OBErrorResponseException(HttpStatus status, OBRIErrorResponseCategory category, OBError1 error) {
        this(status, category, Collections.singletonList(error));
    }

    public OBErrorResponseException(HttpStatus status, OBRIErrorResponseCategory category, String id, OBError1 error) {
        this(status, category, id, Collections.singletonList(error));
    }

    public OBErrorResponseException(HttpStatus status, ErrorResponseTmp errorResponseTmp) {
        this.status = status;
        this.errorResponseTmp = errorResponseTmp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ErrorResponseTmp getErrorResponseTmp() {
        return errorResponseTmp;
    }

    public void setErrorResponseTmp(ErrorResponseTmp errorResponseTmp) {
        this.errorResponseTmp = errorResponseTmp;
    }

    public List<OBError1> getErrors() {
        return errors;
    }

    public OBRIErrorResponseCategory getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }
}

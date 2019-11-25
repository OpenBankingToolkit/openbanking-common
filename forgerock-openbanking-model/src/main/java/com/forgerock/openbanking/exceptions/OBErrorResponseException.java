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
package com.forgerock.openbanking.exceptions;

import com.forgerock.openbanking.model.error.ErrorResponseTmp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
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

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
package com.forgerock.openbanking.exceptions;

import com.forgerock.openbanking.model.error.OBRIErrorType;
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

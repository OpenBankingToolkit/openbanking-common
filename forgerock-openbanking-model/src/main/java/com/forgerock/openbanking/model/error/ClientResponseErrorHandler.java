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
package com.forgerock.openbanking.model.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        switch (statusCode.series()) {
            case CLIENT_ERROR:
                Charset charset = getCharset(response);
                byte[] responseBody = getResponseBody(response);
                String statusText = (StringUtils.isEmpty(response.getStatusText()))
                        ? getResponseBodyAsString(responseBody, charset)   // Don't add leading ':' if status text is empty
                        : response.getStatusText() + " : " + getResponseBodyAsString(responseBody, charset);
                throw new HttpClientErrorException(statusCode, statusText, response.getHeaders(), responseBody, charset);
            default:
                super.handleError(response, statusCode);
        }
    }

    public String getResponseBodyAsString(byte[] responseBody, Charset charset) {
        if (charset == null) {
            return new String(responseBody, StandardCharsets.ISO_8859_1);
        }
        return new String(responseBody, charset);
    }
}

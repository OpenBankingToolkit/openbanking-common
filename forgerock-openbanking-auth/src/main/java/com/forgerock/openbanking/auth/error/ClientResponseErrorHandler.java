/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.error;

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

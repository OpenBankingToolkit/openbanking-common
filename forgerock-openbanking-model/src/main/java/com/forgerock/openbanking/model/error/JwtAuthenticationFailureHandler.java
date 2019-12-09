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

import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.exceptions.AuthMethodNotSupportedException;
import com.forgerock.openbanking.exceptions.JwtExpiredTokenException;
import com.forgerock.openbanking.exceptions.OBErrorAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;
    @Autowired
    private Tracer tracer;
    @Autowired
    public JwtAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }	
    
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		if (e instanceof BadCredentialsException) {
			mapper.writeValue(response.getWriter(),
					ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credential"));
		} else if (e instanceof JwtExpiredTokenException) {
			mapper.writeValue(response.getWriter(), ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT expired"));
		} else if (e instanceof AuthMethodNotSupportedException) {
			mapper.writeValue(response.getWriter(), ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()));
		} else if (e instanceof OBErrorAuthenticationException) {
            OBErrorAuthenticationException obErrorAuthenticationException = (OBErrorAuthenticationException) e;
            HttpStatus httpStatus = obErrorAuthenticationException.getObriErrorType().getHttpStatus();
            OBErrorResponse1 obErrorResponse =
                    new OBErrorResponse1()
                            .code(httpStatus.name())
                            .id(String.valueOf(tracer.currentSpan().context().traceIdString()))
                            .message(httpStatus.getReasonPhrase())
                            .errors(Collections.singletonList(obErrorAuthenticationException.getOBError()));
            mapper.writeValue(response.getWriter(), obErrorResponse);
        }
    }

	public static interface JwtToken {
        String getToken();
    }

	public static class RawAccessJwtToken implements JwtToken {

        public enum Type {
            SSO_TOKEN, ID_TOKEN, ACCESS_TOKEN, REFRESH_TOKEN
        }

        private String token;
        private Type type;

        public RawAccessJwtToken(Type type, String token) {
            this.type = type;
            this.token = token;
        }

        @Override
        public String getToken() {
            return token;
        }

        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return "RawAccessJwtToken{" +
                    "token='" + token + '\'' +
                    ", type=" + type +
                    '}';
        }
    }
}

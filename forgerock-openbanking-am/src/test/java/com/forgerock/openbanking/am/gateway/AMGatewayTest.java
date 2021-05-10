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
package com.forgerock.openbanking.am.gateway;


import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.security.cert.X509Certificate;
import java.util.Collections;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AMGatewayTest {

    public static final String TRUSTED_HEADER = "trustedHeader";
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private AMGateway amGateway;

    @Before
    public void setUp() throws Exception {
        amGateway = new AMGateway(restTemplate, "http://am", "localhost", TRUSTED_HEADER);
    }

    @Test
    public void shouldAddTrustedCertificateHeader() {
        // Given
        X509Certificate x509Certificate = mock(X509Certificate.class);
        SecurityContextHolder.getContext().setAuthentication(new X509Authentication("test", Collections.emptyList(), new X509Certificate[]{x509Certificate}));
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        given(httpServletRequest.getRequestURL()).willReturn(new StringBuffer("http://google.com"));
        given(httpServletRequest.getHeaderNames()).willReturn(Collections.emptyEnumeration());
        given(httpServletRequest.getMethod()).willReturn(HttpMethod.POST.toString());
        HttpHeaders headers = new HttpHeaders();

        // When
        amGateway.toAM(httpServletRequest, headers, Collections.emptyMap(), new ParameterizedTypeReference<String>() {
        }, "");

        // Then
        ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(any(), any(), httpEntityCaptor.capture(), eq(new ParameterizedTypeReference<String>(){}));
        assertThat(httpEntityCaptor.getValue()).isNotNull();
        assertThat(httpEntityCaptor.getValue().getHeaders()).containsKeys(TRUSTED_HEADER);
    }
}

/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.aspsp;

import com.forgerock.openbanking.auth.model.UserContext;
import com.nimbusds.jose.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateEncodingException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.net.HttpHeaders.*;

@Slf4j
public class AMGateway {

    private RestTemplate restTemplate;
    private String amRoot;
    private String hostname;
    private String trustedHeaderCertificate;

    public AMGateway(RestTemplate restTemplate, String amRoot, String hostname, String trustedHeaderCertificate) {
        this.restTemplate = restTemplate;
        this.amRoot = amRoot;
        this.hostname = hostname;
        this.trustedHeaderCertificate = trustedHeaderCertificate;
    }

    public ResponseEntity toAM(HttpServletRequest httpServletRequest, HttpHeaders additionalHttpHeaders, ParameterizedTypeReference parameterizedTypeReference) {
        return toAM(httpServletRequest, additionalHttpHeaders, Collections.emptyMap(), parameterizedTypeReference, null);
    }

    public ResponseEntity toAM(HttpServletRequest httpServletRequest, HttpHeaders additionalHttpHeaders, ParameterizedTypeReference parameterizedTypeReference, Object body) {
        return toAM(httpServletRequest, additionalHttpHeaders, Collections.emptyMap(), parameterizedTypeReference, body);

    }

    public ResponseEntity toAM(HttpServletRequest httpServletRequest, HttpHeaders additionalHttpHeaders, Map<String, String> queryParametersOverride, ParameterizedTypeReference parameterizedTypeReference, Object body) {
        ServletServerHttpRequest request = new ServletServerHttpRequest(httpServletRequest);
        final UriComponentsBuilder builder;
        try {
            builder = UriComponentsBuilder
                    .fromHttpRequest(request)
                    .uri(new URI(amRoot));
        } catch (URISyntaxException e) {
            throw new RuntimeException("AM path path is not a URI", e);
        }

        HttpHeaders httpHeaders = request.getHeaders();
        additionalHttpHeaders.entrySet().stream().forEach(e -> httpHeaders.addAll(e.getKey(), e.getValue()));
        httpHeaders.remove("host");
        httpHeaders.remove(X_FORWARDED_HOST);
        httpHeaders.remove(X_FORWARDED_PROTO);
        httpHeaders.remove(X_FORWARDED_PORT);

        httpHeaders.add("host", hostname);
        httpHeaders.add(X_FORWARDED_HOST, hostname);
        httpHeaders.add(X_FORWARDED_PROTO, "https");

        if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserContext) {
            UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userContext.getCertificatesChain() != null
                    && userContext.getCertificatesChain().length > 0) {
                try {
                    httpHeaders.add(trustedHeaderCertificate, Base64.encode(userContext.getCertificatesChain()[0].getEncoded()).toString());
                } catch (CertificateEncodingException e) {
                    log.error("Could not convert X509 cert into a pem", e);
                }
            }
        }

        HttpEntity httpEntity = new HttpEntity(body, httpHeaders);

        try {
            //BUG in the UriComponentsBuilder which encode twice
            Map<String, String> queries = splitQuery(httpServletRequest.getQueryString());
            for (Map.Entry<String, String> query: queries.entrySet()) {
                builder.replaceQueryParam(query.getKey(), URLEncoder.encode(query.getValue(), StandardCharsets.UTF_8.toString()));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Can't parse query string", e);
        }

        queryParametersOverride.forEach(builder::replaceQueryParam);

        //build() encode but badly: the redirect uri resulting of it is half encoded using build()...
        URI uri = builder.build(true).toUri();

        log.debug("Sending request to AM with uri '{}' and headers : '{}'", uri, httpHeaders);
        try {
            return restTemplate.exchange(uri, request.getMethod(), httpEntity, parameterizedTypeReference);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().is4xxClientError()) {
                log.debug("An error happened on the AS: {}", e.getResponseBodyAsString(), e);
            } else {
                log.error("An error happened on the AS: {}", e.getResponseBodyAsString(), e);
            }
            return new ResponseEntity<>(e.getResponseBodyAsByteArray(), e.getStatusCode());
        }
    }

    public ResponseEntity toAM(String uri, HttpMethod method, HttpHeaders additionalHttpHeaders, ParameterizedTypeReference parameterizedTypeReference, Object body) {
        return toAM(uri, true, method, additionalHttpHeaders, parameterizedTypeReference, body);
    }

    public ResponseEntity toAM(String uri, boolean sendXForwardingHeader, HttpMethod method,
                               HttpHeaders additionalHttpHeaders, ParameterizedTypeReference parameterizedTypeReference, Object body) {

        additionalHttpHeaders.remove("host");
        if (sendXForwardingHeader) {
            additionalHttpHeaders.add("host", hostname);
            additionalHttpHeaders.remove(X_FORWARDED_PORT);
            additionalHttpHeaders.remove(X_FORWARDED_HOST);
            additionalHttpHeaders.remove(X_FORWARDED_PROTO);
            additionalHttpHeaders.add(X_FORWARDED_HOST, hostname);
            additionalHttpHeaders.add(X_FORWARDED_PROTO, "https");
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserContext) {
            UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userContext.getCertificatesChain() != null
                    && userContext.getCertificatesChain().length > 0) {
                try {
                    additionalHttpHeaders.add(trustedHeaderCertificate, Base64.encode(userContext.getCertificatesChain()[0].getEncoded()).toString());
                } catch (CertificateEncodingException e) {
                    log.error("Could not convert X509 cert into a pem", e);
                }
            }
        }

        HttpEntity httpEntity = new HttpEntity(body, additionalHttpHeaders);
        UriComponentsBuilder builder;
        try {
            builder = UriComponentsBuilder
                    .fromUriString(uri)
                    .uri(new URI(amRoot));
        } catch (URISyntaxException e) {
            throw new RuntimeException("AM path path is not a URI", e);
        }
        uri = builder.build().toUriString();

        //BUG in the UriComponentsBuilder which encode twice
        uri = uri.replace("%20", " ");
        log.debug("Sending request to AM with uri '{}' and headers : '{}'", uri, additionalHttpHeaders);

        try {
            return restTemplate.exchange(uri, method, httpEntity, parameterizedTypeReference);
        } catch (HttpStatusCodeException e) {
            log.error("An error happened on the AS: {}", e.getResponseBodyAsString(), e);
            throw e;
        }
    }

    public static Map<String, String> splitQuery(String queryParam) throws UnsupportedEncodingException {
        if (queryParam == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = queryParam.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}

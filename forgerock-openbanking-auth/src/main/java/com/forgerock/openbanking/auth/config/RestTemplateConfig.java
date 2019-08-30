/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.config;

import com.forgerock.openbanking.auth.error.ClientResponseErrorHandler;
import com.forgerock.openbanking.auth.rest.OnBehalfApplicationRestTemplate;
import com.forgerock.openbanking.ssl.config.SslConfiguration;
import com.forgerock.openbanking.ssl.exceptions.SslConfigurationFailure;
import com.forgerock.openbanking.ssl.services.CertificateLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private SslConfiguration sslConfiguration;

    @Value("${server.ssl.client-certs-key-alias}")
    private String keyAlias;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter(@Qualifier("objectMapperBuilderCustomizer") Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        Jackson2ObjectMapperBuilder objectMapperBuilder = new Jackson2ObjectMapperBuilder();
        objectMapperBuilderCustomizer.customize(objectMapperBuilder);
        converter.setObjectMapper(objectMapperBuilder.build());
        return converter;
    }

    @Bean
    public RestTemplate restTemplate(@Qualifier("mappingJacksonHttpMessageConverter")
                                                 MappingJackson2HttpMessageConverter converter) throws SslConfigurationFailure {
        return forExternalForgeRockApplication(converter);
    }

    @Bean(name="forExternalForgeRockApplication")
    public RestTemplate forExternalForgeRockApplication(@Qualifier("mappingJacksonHttpMessageConverter")
                                                                    MappingJackson2HttpMessageConverter converter) throws SslConfigurationFailure {
        RestTemplate restTemplate = new RestTemplate(sslConfiguration.factory(keyAlias, true));
        customiseRestTemplate(converter, restTemplate);
        return restTemplate;
    }

    private void customiseRestTemplate(@Qualifier("mappingJacksonHttpMessageConverter") MappingJackson2HttpMessageConverter converter, RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        messageConverters.add(converter);
        restTemplate.setErrorHandler(new ClientResponseErrorHandler());
    }

    @Bean(name="forExternal")
    public ApplicationRestTemplate forExternal(@Qualifier("mappingJacksonHttpMessageConverter")
                                                           MappingJackson2HttpMessageConverter converter,
                                               CertificateLoader certificateLoader) throws SslConfigurationFailure {
        ApplicationRestTemplate restTemplate = new ApplicationRestTemplate(certificateLoader, sslConfiguration, sslConfiguration.factory(keyAlias, true), true);
        customiseRestTemplate(converter, restTemplate);
        return restTemplate;
    }

    @Bean(name="onBehalfApplication")
    public OnBehalfApplicationRestTemplate onBehalfApplicationForServiceRegistry(@Qualifier("mappingJacksonHttpMessageConverter")
                                                                                             MappingJackson2HttpMessageConverter converter) throws SslConfigurationFailure {
        OnBehalfApplicationRestTemplate restTemplate = new OnBehalfApplicationRestTemplate(sslConfiguration.factory(keyAlias, false), false);
        customiseRestTemplate(converter, restTemplate);
        return restTemplate;
    }

}

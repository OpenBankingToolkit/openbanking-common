/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class ApplicationRestTemplateConfig {

    private static final int RETRY_INITIAL_INTERVAL = 1000;
    private static final int RETRY_MAX_ATTEMPTS = 30;
    private static final double RETRY_EXPONENTIAL_MULTIPLIER = 1.1;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(RETRY_INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(RETRY_EXPONENTIAL_MULTIPLIER);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(RETRY_MAX_ATTEMPTS));
        return retryTemplate;
    }

}

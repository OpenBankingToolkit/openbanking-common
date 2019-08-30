/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.error;

/**
 * The different error code we currently return.
 */
public class ResponseCode {

    public static class ErrorCode {
        public static final Integer INVALID_JWT = 1;
        public static final Integer AM_JWKS_NOT_AVAILABLE = 2;
        public static final Integer INVALID_ACCESS_TOKEN_RESPONSE = 3;
        public static final Integer INVALID_AUTHORIZATION_CODE = 4;
        public static final Integer INVALID_INTENT_ID = 5;
        public static final Integer INVALID_C_HASH = 6;
        public static final Integer INVALID_S_HASH = 7;
        public static final Integer INVALID_ACCESS_TOKEN = 8;
        public static final Integer INVALID_SCOPE = 9;
        public static final Integer INVALID_ID_TOKEN = 10;
        public static final Integer REQUEST_PARAMETER_GENERATATION_FAILURE = 11;
        public static final Integer PAYMENT_REGISTRATION_FAILED = 12;
        public static final Integer INVALID_CLIENT_AUTHENTICATION_JWT = 13;
        public static final Integer INVALID_FINANCIAL_ID = 14;
        public static final Integer INVALID_INITIATION = 15;
        public static final Integer INVALID_RISK = 16;
        public static final Integer PAYMENT_ALREADY_SUBMITTED = 17;
        public static final Integer PAYMENT_REJECTED = 18;
        public static final Integer PAYMENT_STILL_PENDING = 19;
        public static final Integer PAYMENT_WAITING_PSU_CONSENT = 20;
        public static final Integer ACCOUNT_REQUEST_WAITING_PSU_CONSENT = 21;
        public static final Integer ACCOUNT_REQUEST_REJECTED = 22;
        public static final Integer ACCOUNT_REQUEST_REVOKED = 23;
        public static final Integer INVALID_ACCOUNT_REQUEST_ID = 24;
        public static final Integer AISP_CONTEXT_JWT_GENERATION = 25;
        public static final Integer INVALID_AISP_CONTEXT = 26;
        public static final Integer ASPSP_RS_ERROR = 27;
        public static final Integer INVALID_PAGE_NUMBER = 28;
        public static final Integer PERMISSIONS = 29;
        public static final Integer PAYMENT_ID_INVALID = 30;
        public static final Integer INVALID_TPP_ID = 31;
        public static final Integer DENIED_FROM_BOOKING_DATE = 32;
        public static final Integer ACCOUNT_REQUEST_EXPIRED = 33;
        public static final Integer ALREADY_REGISTERED = 34;
        public static final Integer ASPSP_CONFIG_NOT_FOUND = 35;
        public static final Integer INVALID_STATE = 36;
        public static final Integer INTERNAL_SERVER_ERROR = 37;
        public static final Integer NON_AUTHORISED_ACCOUNT = 38;
        public static final Integer BOTH_BASIC_AND_DETAILS = 39;
    }
}

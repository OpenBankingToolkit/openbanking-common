/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.error;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.forgerock.openbanking.auth.serialiser.OBRIErrorTypeDeserializer;
import com.forgerock.openbanking.auth.serialiser.OBRIErrorTypeSerializer;
import org.springframework.http.HttpStatus;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.error.OBError1;
import uk.org.openbanking.datamodel.error.OBStandardErrorCodes1;
import uk.org.openbanking.datamodel.error.StandardErrorCode;

@JsonDeserialize(using = OBRIErrorTypeDeserializer.class)
@JsonSerialize(using = OBRIErrorTypeSerializer.class)
public enum OBRIErrorType {

    INVALID_PAGE_NUMBER(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_PARAMETER_INVALID,
            "Invalid page number",
            "page"),

    INVALID_RESOURCE(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_RESOURCE_INVALID_FORMAT,
            "Invalid resource: %s"),

    // See HTTP codes in spec: https://openbanking.atlassian.net/wiki/spaces/DZ/pages/937656404/Read+Write+Data+API+Specification+-+v3.1#Read/WriteDataAPISpecification-v3.1-HTTPStatusCodes
    ACCESS_TOKEN_INVALID_SCOPE(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_ACCESS_TOKEN_INVALID,
            "Invalid access token. Missing scopes: %s"),
    ACCESS_TOKEN_INVALID_FORMAT(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_ACCESS_TOKEN_INVALID,
            "Invalid access token. Invalid format"),
    ACCESS_TOKEN_INVALID(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_ACCESS_TOKEN_INVALID,
            "Invalid access token. Reason: %s"),
    ACCESS_TOKEN_INVALID_ACCOUNT_REQUEST (
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_ACCESS_TOKEN_INVALID,
            "Invalid access token. The access token is associated with an account request '%s' that doesn't exist"),
    ACCESS_TOKEN_INVALID_PAYMENT_ID (
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_ACCESS_TOKEN_INVALID,
            "Invalid access token. The access token is associated with a payment Id '%s' different from the payment request payment Id '%s'"),
    ACCESS_TOKEN_INVALID_GRANT_TYPE(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_ACCESS_TOKEN_INVALID,
            "The access token grant type %s doesn't match one of the expected grant types %s"),
    FINANCIAL_ID_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_ACCESS_TOKEN_INVALID,
            "Invalid Financial ID. Our ASPSP financial ID '%s' doesn't match the financial ID received '%s'",
            "x-fapi-financial-id"),
    IDEMPOTENCY_KEY_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.FR_OBRI_IDEMPOTENCY_KEY_INVALID,
            "Invalid Idempotency Key provided in header. The "+ OBHeaders.X_IDEMPOTENCY_KEY+" in the request headers must be between 1 and 40 characters. Provided value: '%s' has length: %d"),
    IDEMPOTENCY_KEY_EXPIRED(
            HttpStatus.BAD_REQUEST,
            ErrorCode.FR_OBRI_IDEMPOTENCY_KEY_EXPIRED,
            "Expired Idempotency Key provided in header. The provided "+ OBHeaders.X_IDEMPOTENCY_KEY+": '%s' in the request headers was first used on a request with id: %s on %s. The expiry time is: %d hours"),
    IDEMPOTENCY_KEY_REQUEST_BODY_CHANGED(
            HttpStatus.BAD_REQUEST,
            ErrorCode.FR_OBRI_IDEMPOTENCY_KEY_REQUEST_BODY_CHANGED,
            "The provided Idempotency Key: '%s' header matched a previous request but the request body has been changed. The previous request body with id: '%s' was '%s'. The request body you submitted was: '%s'"),
    DETACHED_JWS_INVALID(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_DETACHED_JWS_INVALID,
            "Invalid detached signature %s. Reason: %s"),
    DETACHED_JWS_UN_ACCESSIBLE(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.DETACHED_JWS_UN_ACCESSIBLE,
            "Can't read the request body"),

    MATLS_TPP_AUTHENTICATION_INVALID_FROM_ACCOUNT_REQUEST (
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_MATLS_UNAUTHORISED,
            "TPP Authentication failed. Your transport certificate is associated with the TPP %s, " +
                    "whereas the account request is associated with %s"),
    MATLS_TPP_AUTHENTICATION_INVALID_FROM_ACCESS_TOKEN (
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_MATLS_UNAUTHORISED,
            "TPP Authentication failed. Your transport certificate is associated with the TPP %s, " +
                    "whereas the access token is associated with %s"),

    ACCOUNT_REQUEST_WAITING_PSU_CONSENT(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_ACCOUNT_REQUEST_INVALID,
            "Account request hasn't been authorised by the PSU yet. Account request status: '%s'"),
    ACCOUNT_REQUEST_REJECTED(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_ACCOUNT_REQUEST_INVALID,
            "Account request has been rejected. Account request status: '%s'"),
    ACCOUNT_REQUEST_REVOKED(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_ACCOUNT_REQUEST_INVALID,
            "Account request has been revoked. Account request status: '%s'"),
    ACCOUNT_REQUEST_EXPIRED(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_ACCOUNT_REQUEST_INVALID,
            "Account request has expired. Account request expiration date: '%s'"),
    ACCOUNT_REQUEST_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_ACCOUNT_REQUEST_NOT_FOUND,
            "Account request '%s' not found."),


    UNAUTHORISED_ACCOUNT(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_ACCOUNT_INVALID,
            "You are not authorised to access account '%s'. The account request '%s' only authorised the following accounts: '%s'"),

    PERMISSIONS_INVALID(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_PERMISSION_INVALID,
            "Invalid permissions. To access this resources, you need the following minimum permissions : '%s'. Your account request only authorised '%s'"),
    PERMISSIONS_TRANSACTIONS_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_PERMISSION_INVALID,
            "Invalid permissions. %s"),

    PAYMENT_ALREADY_SUBMITTED(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Payment has already been submitted. Payment request status: '%s'"),
    PAYMENT_FILE_ALREADY_SUBMITTED(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Payment file has already been submitted for this consent. Payment request status: '%s'"),
    PAYMENT_WAITING_PSU_CONSENT(
            HttpStatus.NOT_ACCEPTABLE,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Payment request hasn't been authorised by the PSU yet. Payment request status: '%s'"),
    PAYMENT_STILL_PENDING(
            HttpStatus.NOT_ACCEPTABLE,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Payment still pending. Payment request status: '%s'"),
    PAYMENT_REJECTED(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Payment has been rejected. Payment request status: '%s'"),
    PAYMENT_INVALID_INITIATION(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Payment initiation received doesn't match the initial payment request"),
    PAYMENT_INVALID_RISK(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Payment risk received doesn't match the risk payment request"),
    PAYMENT_SUBMISSION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_PAYMENT_NOT_FOUND,
            "Payment submission '%s' can't be found"),
    PAYMENT_INVALID_EXCHANGE_RATE_TYPE(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Unknown exchange rate type: '%s'"),
    PAYMENT_INVALID_EXCHANGE_RATE(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment invalid. Invalid exchange rate information provided"),
    PAYMENT_SUBMISSION_ALREADY_EXISTS(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "Payment submission invalid. Payment submission already exists. Payment Id: '%s'"),
    PAYMENT_ID_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_PAYMENT_NOT_FOUND,
            "Payment ID '%s' not found"),
    PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_PAYMENT_NOT_FOUND,
            "Payment consent behind payment submission '%s' can't be found"),
    CONSENT_STATUS_NOT_AUTHORISED(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_INVALID_CONSENT_STATUS,
            "Confirmation is not allowed unless the consent status is Authorised. Currently, the consent is %s"),
    PAYMENT_BALANCE_TRANSFER_INVALID_CREDITOR_ACCOUNT(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'Initiation.CreditorAccount.SchemeName' for a Balance Transfer payment consent is missing or has the wrong type. Creditor account must of type: Primary Account Number (UK.OBIE.PAN)"),
    PAYMENT_BALANCE_TRANSFER_INVALID_DEBTOR_ACCOUNT(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'Initiation.DebtorAccount.SchemeName' for a Balance Transfer payment consent is missing or has the wrong identification type. Debtor account must of type: Primary Account Number (UK.OBIE.PAN)"),
    PAYMENT_BALANCE_TRANSFER_INVALID_REMITTANCE_REFERENCE(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'Initiation.RemittanceInformation.Reference' field for a Balance Transfer payment consent is missing. This field is mandatory for a Balance Transfer payment."),
    PAYMENT_MONEY_TRANSFER_INVALID_CREDITOR_ACCOUNT(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'Initiation.CreditorAccount.SchemeName' for a Money Transfer payment consent is missing or has the wrong type. Creditor account must of type: Primary Account Number (UK.OBIE.SortCodeAccountNumber)"),
    PAYMENT_MONEY_TRANSFER_INVALID_DEBTOR_ACCOUNT(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'Initiation.DebtorAccount.SchemeName' for a Money Transfer payment consent is missing or has the wrong identification type. Debtor account must of type: Primary Account Number (UK.OBIE.PAN)"),
    PAYMENT_MONEY_TRANSFER_INVALID_REMITTANCE_REFERENCE(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'Initiation.RemittanceInformation.Reference' field for a Money Transfer payment consent is missing. This field is mandatory for a Money Transfer payment."),
    PAYMENT_PAYM_INVALID_PAYMENT_CONTEXT_CODE(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'Risk.PaymentContextCode' for a Paym payment consent was invalid. Payment Context Code account must 'PartyToParty'"),
    PAYMENT_PAYM_INVALID_LOCAL_INSTRUMENT(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'Initiation.LocalInstrument' for Paym payment consent is invalid. It must be empty or set to 'Paym'"),
    PAYMENT_PAYM_WRONG_CREDITOR_ACCOUNT_SCHEME(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'CreditorAccount.SchemeName' field for a Paym payment consent is invalid. This field must be 'UK.OBIE.Paym'."),
    PAYMENT_PAYM_MISSING_CUSTOMER_IDENTIFICATION(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The 'CreditorAccount.Identification' field for a Paym payment consent is missing. This field is mandatory for a Paym payment and should identify the mobile number."),

    INVALID_CALLBACK_URL(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "Callback URL '%s' is not valid. It must be a valid URL and end in /{version}/event-notifications"),
    INVALID_CALLBACK_URL_VERSION(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "Callback URL version is not valid. The new callback URL version: '%s' must be equal to or newer than the existing callback URL: '%s'"),
    LONG_POLLING_NOT_SUPPORTED_FOR_EVENTS(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "Long polling is not currently supported on this sandbox. Please set the 'returnImmediately' field to true"),
    EVENT_SUBSCRIPTION_ALREADY_EXISTS(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_EVENT_SUBSCRIPTION_EXISTS,
            "An event subscription already exists for this TPP. Please use PUT and DELETE methods to amend or remove."),
    EVENT_SUBSCRIPTION_NOT_FOUND(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_EVENT_SUBSCRIPTION_EXISTS,
            "An event subscription could not be found with id: %s."),
    CALLBACK_URL_ALREADY_EXISTS(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_CALLBACK_URL_EXISTS,
            "An callback URL of '%s' already exists for this TPP. Please use PUT and DELETE methods to amend or remove."),
    CALLBACK_URL_NOT_FOUND(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_CALLBACK_URL_NOT_FOUND,
            "An callback URL could not be found with id: %s."),

    FUNDS_CONFIRMATION_STILL_PENDING(
            HttpStatus.NOT_ACCEPTABLE,
            ErrorCode.OBRI_FUNDS_CONFIRMATION_INVALID,
            "Funds confirmation invalid. Funds confirmation still pending. Funds confirmation request status: '%s'"),
    FUNDS_CONFIRMATION_REJECTED(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_FUNDS_CONFIRMATION_INVALID,
            "Funds confirmation invalid. Funds confirmation has been rejected. Funds confirmation request status: '%s'"),
    FUNDS_CONFIRMATION_EXPIRED(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_FUNDS_CONFIRMATION_INVALID,
            "Funds confirmation invalid. Funds confirmation has expired. Funds confirmation expiration date/time: '%s'"),

    TPP_REGISTRATION_ALREADY_REGISTERED(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REGISTRATION_INVALID,
            "Your TPP '%s' is already registered."),
    TPP_REGISTRATION_UNKNOWN_TRANSPORT_CERTIFICATE(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REGISTRATION_CERTIFICATE_TRANSPORT_INVALID,
            "Unknown transport certificate. Could not authentication you with your transport certificate. CN '%s'."),
    TPP_REGISTRATION_TRANSPORT_CERTIFICATE_NOT_MATCHING_SSA(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REGISTRATION_CERTIFICATE_TRANSPORT_INVALID,
            "SSA software ID '%s' doesn't match your transport certificate CN '%s'"),
    TPP_REGISTRATION_REQUEST_JWT_INVALID(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REGISTRATION_REQUEST_INVALID,
            "The registration request JWT is invalid. Reason '%s'"),
    TPP_REGISTRATION_REQUEST_NOT_MATCHING_SSA(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REGISTRATION_REQUEST_INVALID,
            "Some of your registration request JWT claims doesn't match the SSA: %s"),
    TPP_REGISTRATION_REQUEST_INVALID_FORMAT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_REQUEST_INVALID,
            "Invalid TPP registration request JWT format"),
    TPP_REGISTRATION_NOT_REGISTERED(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_REGISTRATION_REQUEST_INVALID,
            "TPP '%s' is not registered"),
    TPP_REGISTRATION_OIDC_CLIENT_REGISTRATION_ISSUE(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_AS_ERROR,
            "An error happened when registering your OIDC client: %s"),
    TPP_REGISTRATION_INVALID_ACCESS_TOKEN(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REGISTRATION_ACCESS_TOKEN_INVALID,
            "The client does not have permission to read, update or delete the Client"),
    TPP_REGISTRATION_INVALID_OIDC_CLIENT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_ACCESS_TOKEN_INVALID,
            "The OIDC client %s is not matching your current TPP (behind your transport cert) OIDC client %s"),
    TPP_REGISTRATION_INVALID_AUTH_METHOD(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_AUTHENTICATION_METHOD,
            "The token endpoint authentication method %s is not supported."),
    TPP_NOT_FOUND(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_PAYMENT_INVALID,
            "A TPP with the provided id: '%s' could not be not found."),
    PRINCIPAL_MISSING_PSD2_INFORMATION(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_PSD2_CERTIFICATE_PRINCIPAL_MISSING,
            "Principal did not contain eIDAS/PSD2 information"),

    OBRI_REGISTRATION_NO_SIGNING_KEYS(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_NO_SIGNING_KEY,
            "Unable to determine the signing key from the EIDAS flow in dynamic registration"
    ),
    OBRI_REGISTRATION_EIDAS_CERTIFICATE_READ_ISSUE(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_CERTIFICATE_ISSUE,
            "Couldn't read the EIDAS/PSD2 certificate"
    ),
    OBRI_REGISTRATION_EIDAS_CONVERT_TO_JWK(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_REGISTRATION_CERTIFICATE_ISSUE,
            "Couldn't convert the EIDAS/PSD2 certificate into a JWK"
    ),
    OBRI_REGISTRATION_NOT_PSD2(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_CERTIFICATE_ISSUE,
            "Your signing certificate is not recognised as a PSD2 certificates"
    ),
    OBRI_REGISTRATION_NOT_KNOWN_EIDAS(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_CERTIFICATE_ISSUE,
            "Your signing certificate is not a recognised EIDAS certificates"
    ),
    OBRI_REGISTRATION_NOT_QSEAL(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_CERTIFICATE_ISSUE,
            "You need to sign the registration JWT with your QSEAL certificate. Instead you used a %s certificate."
    ),
    OBRI_REGISTRATION_QSEAL_NOT_MATCHING_QWAC(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_REGISTRATION_CERTIFICATE_ISSUE,
            "The organisation ID %s from the QWAC certificate is not matching the organisation ID %s from the QSEAL"
    ),

    TPP_REGISTRATION_SSA_INVALID(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REGISTRATION_SSA_INVALID,
            "Invalid SSA signature."),
    TPP_REGISTRATION_SSA_INVALID_FORMAT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REGISTRATION_SSA_INVALID,
            "Invalid SSA JWT format"),
    REQUEST_FILE_INCORRECT_FILE_HASH(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_FILE_HASH_INCORRECT,
            "The file received has a hash of '%s' but the file consent metadata indicated that we are expecting a file hash of: '%s'"),
    REQUEST_FILE_WRONG_NUMBER_OF_TRANSACTIONS(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_FILE_WRONG_NUMBER_OF_TRANSACTIONS,
            "The file received contains %s transactions but the file consent metadata indicated that we are expecting a file with %s transactions'"),
    REQUEST_FILE_INCORRECT_CONTROL_SUM(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_FILE_WRONG_CONTROL_SUM,
            "The file received contains total transaction value of: %s but the file consent metadata indicated a control sum value of %s'"),
    REQUEST_FILE_EMPTY(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_FILE_EMPTY,
            "The file received is empty."),
    REQUEST_FILE_XML_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_FILE_INVALID_XML,
            "The file received was not parsable as valid XML. Reason %s"),
    REQUEST_FILE_JSON_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_FILE_INVALID_JSON,
            "The file received was not parsable as valid JSON. Reason %s"),
    FILE_PAYMENT_REPORT_NOT_READY(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_FILE_INVALID_JSON,
            "A report file was requested for payment: %s but this payment has not been processed yet. Payment status is: %s. Valid statuses are: %s"),


    REQUEST_FIELD_INVALID(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_FIELD_INVALID,
            "The field received is invalid. Reason '%s'"),
    REQUEST_OBJECT_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_GLOBAL_OBJECT_INVALID,
            "The object received is invalid. Reason '%s'"),
    REQUEST_PARAMETER_JWT_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_PARAMETER_JWT_INVALID,
            "The request parameter JWT is invalid. Reason '%s'"),
    REQUEST_PARAMETER_JWT_FORMAT_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_PARAMETER_JWT_INVALID,
            "The format of the request parameter JWT is invalid. Reason '%s'"),
    REQUEST_PARAMETER_CLAIM_MANDATORY(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_PARAMETER_CLAIM_MANDATORY,
            "The following claim '%s' is mandatory in the request parameter JWT."),
    REQUEST_PARAMETER_QUERY_PARAM_DIFF_CLAIM(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_PARAMETER_QUERY_NOT_MATCHING_CLAIM,
            "If defined, query parameter '%s' should match the request parameter claim '%s'."),

    REQUEST_PARAMETER_MISSING(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_PARAMETER_MISSING,
            "The field '%s' is missing"),
    REQUEST_BINDING_FAILED(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_BINDING_FAILED,
            "Request binding failed. reason: %s"),
    REQUEST_ARGUMENT_TYPE_MISMATCH(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_ARGUMENT_TYPE_MISMATCH,
            "The argument %s should be of type %s"),
    REQUEST_METHOD_NOT_SUPPORTED(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_METHOD_NOT_SUPPORTED,
            "Method '%s' is not supported for this request. Supported methods are %s"),
    REQUEST_MEDIA_TYPE_NOT_SUPPORTED(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            ErrorCode.OBRI_REQUEST_MEDIA_TYPE_NOT_SUPPORTED,
            "Media type '%s' is not supported for this request. Supported media type are %s"),
    REQUEST_MEDIA_TYPE_NOT_ACCEPTABLE(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            ErrorCode.OBRI_REQUEST_MEDIA_TYPE_NOT_ACCEPTABLE,
            "Could not find acceptable representation. Supported media type are %s"),
    REQUEST_MESSAGE_NOT_READABLE(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_MESSAGE_NOT_READABLE,
            "Malformed JSON request. %s"),
    REQUEST_PATH_VARIABLE_MISSING(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_REQUEST_MISSING_PATH_VARIABLE,
            "The path variable '%s' is missing for method %s"),
    REQUEST_MISSING_HEADER(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_HEADER_MISSING,
            "%s"),
    REQUEST_MISSING_COOKIE(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_MISSING_COOKIE,
            "%s"),
    REQUEST_MISSING_ARGUMENT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_MISSING_ARGUMENT,
            "%s"),
    REQUEST_INVALID_HEADER(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_HEADER_INVALID,
            "%s"),
    REQUEST_EXPIRED_X_IDEMPOTENCY_KEY(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_HEADER_INVALID,
            "The provided x-idempotency-key header in request is expired. DateTime now: %s , DateTime this key was first used on a request: %s "),
    REQUEST_MATCHED_X_IDEMPOTENCY_KEY_ON_UNEQUAL_CONSENT(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_HEADER_INVALID,
            "The provided x-idempotency-key header matched an existing consent with id: {} but the consent details in request were different. Provided request body: %s , Existing consent: %s "),
    REQUEST_MATCHED_X_IDEMPOTENCY_KEY_ON_UNEQUAL_SUBMISSION(
            HttpStatus.BAD_REQUEST,
            OBStandardErrorCodes1.UK_OBIE_HEADER_INVALID,
            "The provided x-idempotency-key header matched an existing payment submission with id: {} but the payment submission details in request were different. Provided request body: %s , Existing payment submission: %s "),
    REQUEST_ACCOUNT_ACCESS_CONSENT_PERMISSIONS_ARE_NOT_PERMITTED(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_PARAMETER_PERMISSIONS_NOT_PERMITTED,
            "One or more permissions requested in the submitted account access consent request are not permitted on this sandbox. The permissions that are not permitted are: %s"),

    REQUEST_UNDEFINED_ERROR_YET(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_REQUEST_UNDEFINED_ERROR_YET,
            "Undefined error yet. Generic error message: %s"),
    SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_SERVER_INTERNAL_ERROR,
            "Internal Server Error"),

    DATA_INVALID_REQUEST(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_DATA_REQUEST_INVALID,
            "Your data request is invalid: reason %s"),
    CLIENT_CERTIFICATE_NOT_PROVIDED(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REQUEST_FORBIDDEN,
            "During authentication, the client certificate used has been bind to your user session. You need to provide the same client certificate each time you use this session"),
    CLIENT_CERTIFICATE_NOT_MATCH(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_REQUEST_FORBIDDEN,
            "During authentication, the client certificate used has been bind to your user session. The client certificate provided doesn't match the certificate hash associated with your user session"),


    ID_TOKEN_INVALID_FORMAT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_ID_TOKEN_INVALID,
            "Invalid ID token. Invalid format"),
    ID_TOKEN_INVALID(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_ID_TOKEN_INVALID,
            "Invalid ID token. Reason: %s"),

    RCS_CONSENT_REQUEST_FORMAT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_RCS_CONSENT_REQUEST_INVALID,
            "Invalid remote consent request token. Invalid format"),
    RCS_CONSENT_REQUEST_INVALID(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_RCS_CONSENT_REQUEST_INVALID,
            "Invalid Remote consent request token. Reason: %s"),
    RCS_CONSENT_RESPONSE_FAILURE(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_SERVER_INTERNAL_ERROR,
            "Could not generate consent response JWT"),
    RCS_CONSENT_REQUEST_UNKNOWN_ACCOUNT_REQUEST(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_RCS_CONSENT_REQUEST_INVALID,
            "The AISP is referencing an account request %s that doesn't exist"),
    RCS_CONSENT_REQUEST_INVALID_CONSENT(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_RCS_CONSENT_REQUEST_INVALID,
            "The TPP '%s' created the account request '%s' but it's TPP '%s' that is trying to get" +
                    " consent for it."),
    RCS_CONSENT_REQUEST_DEBTOR_ACCOUNT_NOT_FOUND(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_RCS_CONSENT_REQUEST_INVALID,
            "The TPP '%s' specified a debtor account in the consent of: '%s' but it was not found in the list of accounts '%s' for the PSU."),

    RCS_CONSENT_REQUEST_INVALID_PAYMENT_REQUEST(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_RCS_CONSENT_REQUEST_INVALID,
            "The PISP '%s' created the payment request '%s' but it's PISP '%s' that is requesting" +
                    " consent for it."),
    RCS_CONSENT_REQUEST_NOT_FOUND_TPP(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_RCS_CONSENT_REQUEST_INVALID,
            "The TPP '%s' that created this intent id '%s' doesn't exist anymore."),

    RCS_CONSENT_REQUEST_INVALID_FUNDS_CONFIRMATION_REQUEST(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_RCS_CONSENT_REQUEST_INVALID,
            "The PISP '%s' created the funds confirmation request '%s' but it's PISP '%s' that is trying to get" +
                    " consent for it."),

    RCS_CONSENT_DECISIONS_FORMAT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_RCS_CONSENT_DECISION_INVALID,
            "Invalid remote consent decision token. Invalid format"),

    RCS_CONSENT_DECISION_EMPTY(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_RCS_CONSENT_DECISION_INVALID,
            "Consent decision is empty"),
    RCS_CONSENT_DECISION_INVALID_USER(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_RCS_CONSENT_DECISION_INVALID,
            "The account request was associated with user '%s' but now, its user '%s' that " +
                    "consented the accounts information sharing."),
    RCS_CONSENT_DECISION_INVALID_ACCOUNT(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_RCS_CONSENT_DECISION_INVALID,
            "The PSU %s is trying to share an account '%s' he doesn't own. List of his accounts '%s'"),

    ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID,
            "Couldn't parse client assertion '%s'"),
    ACCESS_TOKEN_NO_CREDENTIAL(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.OBRI_ACCESS_TOKEN_AUTH_METHOD_INVALID,
            "No credential found in the request. Your TPP current authentication method is '%s'."),
    ACCESS_TOKEN_WRONG_AUTH_METHOD(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_ACCESS_TOKEN_AUTH_METHOD_INVALID,
            "You used the authentication method '%s' but your TPP is registered to use '%s'"),
    ACCESS_TOKEN_CREDENTIAL_NOT_MATCHING_CLIENT_CERTS(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_ACCESS_TOKEN_AUTH_METHOD_NOT_MATCHING_CLIENT_CERTIFICATION,
            "Client ID behind transport certificate '%s' doesn't match client ID '%s' used in your credential " +
                    "for the authentication method '%s'"),
    ACCESS_TOKEN_INVALID_ID_TOKEN(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_ACCESS_TOKEN_INVALID_ID_TOKEN,
            "An error happened on the server side, please contact support with ID %s"),

    ACCESS_TOKEN_CLIENT_ID_MISS_MATCH(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_ACCESS_TOKEN_CLIENT_IDS_MISSMATCH,
            "The client ID from the request body '%s' is not matching the client ID extract from the client assertion or client secret basic %s"),

    AUTHORIZE_INVALID_ID_TOKEN(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_AUTHORIZE_INVALID_ID_TOKEN,
            "An error happened on the server side, please contact support with ID %s"),

    MANUAL_ONBOARDING_SOFTWARE_STATEMENT_ALREADY_ONBOARD(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_MANUAL_ONBOARDING_SSA_ALREADY_USER_FOUND,
            "The software statement assertion was already used to on-board another application"),
    MANUAL_ONBOARDING_APPLICATION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_MANUAL_ONBOARDING_APP_NOT_FOUND,
            "The application '%s' can not be found"),
    MANUAL_ONBOARDING_TPP_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            ErrorCode.OBRI_MANUAL_ONBOARDING_TPP_NOT_FOUND,
            "The TPP ID '%s' can not be found"),
    MANUAL_ONBOARDING_WRONG_USER(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_MANUAL_ONBOARDING_TPP_NOT_FOUND,
            "The user '%s' is trying to access the application %s which belongs to another user"),
    MANUAL_ONBOARDING_EIDAS_ALREADY_ONBOARD(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_MANUAL_ONBOARDING_EIDAS_ALREADY_USER_FOUND,
            "Your EIDAS was already used to on-board another application"),

    HEAD_LESS_AUTH_RCS_URI_INCORRECT(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_HEADLESS_RCS_URI_INVALID,
            "The location '%s' to the RCS, returned by AM, is not an URL"),
    HEAD_LESS_AUTH_TPP_URI_INCORRECT(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_HEADLESS_TPP_URI_INVALID,
            "The location '%s' to the TPP, returned by AM, is not an URL"),
    HEAD_LESS_AUTH_TPP_URI_NO_CODE(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_HEADLESS_TPP_NO_CODE,
            "The location '%s' to the TPP, returned by AM, is not an URL"),
    HEAD_LESS_AUTH_AS_ERROR_RECEIVED(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_HEADLESS_AS_ERROR,
            "Error type: '%s', Error description: '%s', State: '%s'"),
    HEAD_LESS_AUTH_EXCHANGE_CODE_BODY_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.OBRI_HEADLESS_EXCHANGE_CODE_ERROR,
            "Couldn't encode the body parameter '%s'"),

    SESSION_TOKEN_INVALID_FORMAT(
            HttpStatus.BAD_REQUEST,
            ErrorCode.OBRI_SESSION_TOKEN_INVALID,
            "Invalid session token. Invalid format"),
    SESSION_TOKEN_EXPIRED(
            HttpStatus.FORBIDDEN,
            ErrorCode.OBRI_SESSION_EXPIRED,
            "Session has expired"),
    ;

    private HttpStatus httpStatus;
    private StandardErrorCode code;
    private String message;
    private String path = null;

    OBRIErrorType(HttpStatus httpStatus, StandardErrorCode code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    OBRIErrorType(HttpStatus httpStatus, StandardErrorCode code, String message, String path) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public StandardErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public OBError1 toOBError1(Object... args) {
        return new OBError1()
                .errorCode(getCode().getValue())
                .message(String.format(getMessage(), args))
                .path(getPath());
    }

}

/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import uk.org.openbanking.datamodel.error.StandardErrorCode;

public enum ErrorCode implements StandardErrorCode {

    OBRI_ACCESS_TOKEN_INVALID("OBRI.AccessToken.Invalid"),
    OBRI_ID_TOKEN_INVALID("OBRI.IDToken.Invalid"),

    OBRI_DETACHED_JWS_INVALID("OBRI.DetachedJws.Invalid"),
    DETACHED_JWS_UN_ACCESSIBLE("OBRI.DetachedJws.BodyNotAccessible"),

    OBRI_MATLS_UNAUTHORISED("OBRI.MATLS.Unauthorised"),

    OBRI_ACCOUNT_REQUEST_INVALID("OBRI.AccountRequest.Invalid"),
    OBRI_ACCOUNT_REQUEST_NOT_FOUND("OBRI.AccountRequest.NotFound"),

    OBRI_PAYMENT_REQUEST_INVALID("OBRI.PaymentRequest.Invalid"),
    OBRI_PAYMENT_REQUEST_NOT_FOUND("OBRI.PaymentRequest.NotFound"),

    OBRI_ACCOUNT_INVALID("OBRI.Account.Invalid"),

    OBRI_PERMISSION_INVALID("OBRI.Permissions.Invalid"),

    OBRI_PAYMENT_INVALID("OBRI.Payment.Invalid"),
    OBRI_FUNDS_CONFIRMATION_INVALID("OBRI.FundsConfirmation.Invalid"),
    OBRI_EVENT_SUBSCRIPTION_EXISTS("OBRI.EventSubscription.Exists"),
    OBRI_EVENT_SUBSCRIPTION_NOT_FOUND("OBRI.EventSubscription.NotFound"),
    OBRI_CALLBACK_URL_EXISTS("OBRI.EventSubscription.Exists"),
    OBRI_CALLBACK_URL_NOT_FOUND("OBRI.CallbackUrl.Exists"),

    OBRI_REGISTRATION_INVALID("OBRI.Registration.Invalid"),
    OBRI_REGISTRATION_CERTIFICATE_TRANSPORT_INVALID("OBRI.Registration.certificate.transport.Invalid"),
    OBRI_REGISTRATION_SSA_INVALID("OBRI.Registration.SSA.Invalid"),
    OBRI_REGISTRATION_REQUEST_INVALID("OBRI.Registration.Request.Invalid"),
    OBRI_REGISTRATION_AS_ERROR("OBRI.Registration.AS.Error"),
    OBRI_REGISTRATION_ACCESS_TOKEN_INVALID("OBRI.Registration.AccessToken.Invalid"),
    OBRI_REGISTRATION_OIDC_CLIENT_ID_INVALID("OBRI.Registration.OIDCClientID.Invalid"),
    OBRI_REGISTRATION_AUTHENTICATION_METHOD("OBRI.Registration.TokenEndpointAuthMethod.Invalid"),
    OBRI_REGISTRATION_NO_SIGNING_KEY( "OBRI.PSD2.Registration.Request.NoSigningKey"),
    OBRI_REGISTRATION_CERTIFICATE_ISSUE( "OBRI.PSD2.Registration.Certificate"),

    OBRI_REQUEST_GLOBAL_OBJECT_INVALID("OBRI.Request.Object.Invalid"),
    OBRI_REQUEST_BINDING_FAILED("OBRI.Request.Binding.Failed"),
    OBRI_REQUEST_ARGUMENT_TYPE_MISMATCH("OBRI.Request.Arguments.Mismatch"),
    OBRI_REQUEST_METHOD_NOT_SUPPORTED("OBRI.Request.Method.NotSupported"),
    OBRI_REQUEST_MEDIA_TYPE_NOT_SUPPORTED("OBRI.Request.MediaType.NotSupported"),
    OBRI_REQUEST_MEDIA_TYPE_NOT_ACCEPTABLE("OBRI.Request.MediaType.NotAcceptable"),
    OBRI_REQUEST_MESSAGE_NOT_READABLE("OBRI.Request.Message.NotReadable"),
    OBRI_REQUEST_MISSING_PATH_VARIABLE("OBRI.Request.PathVariable.missing"),
    OBRI_REQUEST_MISSING_COOKIE("OBRI.Request.Cookie.missing"),
    OBRI_REQUEST_MISSING_ARGUMENT("OBRI.Request.Argument.missing"),
    OBRI_REQUEST_PARAMETER_JWT_INVALID("OBRI.Request.Parameter.Invalid"),
    OBRI_REQUEST_PARAMETER_CLAIM_MANDATORY("OBRI.Request.Parameter.claim.mandatory"),
    OBRI_REQUEST_PARAMETER_QUERY_NOT_MATCHING_CLAIM("OBRI.Request.Parameter.query.not.matching.claim"),
    OBRI_REQUEST_PARAMETER_PERMISSIONS_NOT_PERMITTED("OBRI.Request.Parameter.permissions.not.permitted"),

    OBRI_REQUEST_FILE_HASH_INCORRECT("OBRI.Request.Object.file.hash.no.matching.metadata"),
    OBRI_REQUEST_FILE_EMPTY("OBRI.Request.Object.file.empty"),
    OBRI_REQUEST_FILE_WRONG_NUMBER_OF_TRANSACTIONS("OBRI.Request.Object.file.wrong.number.of.transactions"),
    OBRI_REQUEST_FILE_WRONG_CONTROL_SUM("OBRI.Request.Object.file.wrong.control.sum"),
    OBRI_REQUEST_FILE_INVALID_XML("OBRI.Request.Object.file.invalid.xml"),
    OBRI_REQUEST_FILE_MISSING_XML_ELEMENT("OBRI.Request.Object.file.missing.xml.element"),
    OBRI_REQUEST_FILE_INVALID_JSON("OBRI.Request.Object.file.invalid.json"),
    OBRI_REQUEST_FILE_MISSING_JSON_ELEMENT("OBRI.Request.Object.file.missing.json.element"),

    OBRI_SERVER_INTERNAL_ERROR("OBRI.Server.InternalError"),
    OBRI_REQUEST_UNDEFINED_ERROR_YET("OBRI.Request.ErrorUnknown"),

    OBRI_PAYMENT_NOT_FOUND("OBRI.Payment.NotFound"),

    OBRI_DATA_REQUEST_INVALID("OBRI.Data.Request.Invalid"),
    OBRI_REQUEST_FORBIDDEN("OBRI.Request.Forbidden"),

    OBRI_RCS_CONSENT_REQUEST_INVALID("OBRI.RCS.consent.request.Invalid"),
    OBRI_RCS_CONSENT_DECISION_INVALID("OBRI.RCS.consent.decision.Invalid"),

    OBRI_ACCESS_TOKEN_AUTH_METHOD_INVALID("OBRI.AccessToken.AuthMethod.Invalid"),
    OBRI_ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID("OBRI.AccessToken.ClientAssertion.Format.Invalid"),
    OBRI_ACCESS_TOKEN_CLIENT_IDS_MISSMATCH("OBRI.AccessToken.ClientIDsMissMatch"),
    OBRI_ACCESS_TOKEN_AUTH_METHOD_NOT_MATCHING_CLIENT_CERTIFICATION("OBRI.AccessToken.AuthMethod.NotMatching.ClientCerts"),
    OBRI_ACCESS_TOKEN_INVALID_ID_TOKEN("OBRI.AccessToken.IdToken.Invalid"),

    OBRI_AUTHORIZE_INVALID_ID_TOKEN("OBRI.Authorize.IdToken.Invalid"),

    OBRI_HEADLESS_RCS_URI_INVALID("OBRI.HeadLessAuth.RCS.uri.Invalid"),
    OBRI_HEADLESS_TPP_URI_INVALID("OBRI.HeadLessAuth.TPP.uri.Invalid"),
    OBRI_HEADLESS_TPP_NO_CODE("OBRI.HeadLessAuth.TPP.code.not.found"),
    OBRI_HEADLESS_AS_ERROR("OBRI.HeadLessAuth.AS.error"),
    OBRI_HEADLESS_EXCHANGE_CODE_ERROR("OBRI.HeadLessAuth.ExchangeCode.error"),

    OBRI_MANUAL_ONBOARDING_EIDAS_ALREADY_USER_FOUND("OBRI.AccessToken.ManualOnboarding.EIDAS.AlreadyUsed"),
    OBRI_MANUAL_ONBOARDING_SSA_ALREADY_USER_FOUND("OBRI.AccessToken.ManualOnboarding.SSA.AlreadyUsed"),
    OBRI_MANUAL_ONBOARDING_APP_NOT_FOUND("OBRI.AccessToken.ManualOnboarding.Application.NotFound"),
    OBRI_MANUAL_ONBOARDING_TPP_NOT_FOUND("OBRI.AccessToken.ManualOnboarding.TPP.NotFound"),
    OBRI_MANUAL_ONBOARDING_WRONG_USER("OBRI.AccessToken.ManualOnboarding.WrongUser"),

    OBRI_SESSION_TOKEN_INVALID("OBRI.session.Invalid"),
    OBRI_SESSION_EXPIRED("OBRI.session.expired"),

    FR_OBRI_IDEMPOTENCY_KEY_INVALID("FR.OBRI.idempotency.key.Invalid"),
    FR_OBRI_IDEMPOTENCY_KEY_EXPIRED("FR.OBRI.idempotency.key.Expired"),
    FR_OBRI_IDEMPOTENCY_KEY_REQUEST_BODY_CHANGED("FR.OBRI.idempotency.key.RequestBodyChanged"),

    OBRI_PSD2_CERTIFICATE_PRINCIPAL_MISSING("OBRI.PSD2.Certificate.Principal.Missing"),
    PSD_CERT_MISSING_JWKURI_MSG( "OBRI.PSD2.Certificate.Jwkuri.Missing"),
    ;

    private String value;

    ErrorCode(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ErrorCode fromValue(String text) {
        for (ErrorCode b : ErrorCode.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
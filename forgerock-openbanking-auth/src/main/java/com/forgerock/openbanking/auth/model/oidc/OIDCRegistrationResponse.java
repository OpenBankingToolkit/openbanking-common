/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.oidc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OIDCRegistrationResponse extends OIDCRegistrationRequest {

    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("registration_access_token")
    private String registrationAccessToken;
    @JsonProperty("registration_client_uri")
    private String registrationClientUri;
    @JsonProperty("client_id_issued_at")
    private String clientIdIssuedAt;
    @JsonProperty("client_secret_expires_at")
    private String clientSecretExpiresAt;
}

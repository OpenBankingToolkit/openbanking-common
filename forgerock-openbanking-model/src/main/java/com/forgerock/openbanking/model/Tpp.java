/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.model;

import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Tpp {
    @Id
    @Indexed
    public String id;
    public String directoryId;
    public String name;
    public String officialName;
    @Indexed
    private String certificateCn;
    @Indexed
    private String clientId;
    private String ssa;
    private String tppRequest;
    private OIDCRegistrationResponse registrationResponse;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    private Set<SoftwareStatementRole> types = new HashSet<>();

    public JWTClaimsSet getSsaClaim() throws ParseException {
        return JWTClaimsSet.parse(ssa);
    }

    public String getLogo() {
        if (getSsa() != null) {
            try {
                JWTClaimsSet claims = getSsaClaim();
                return claims.getStringClaim(OpenBankingConstants.SSAClaims.SOFTWARE_LOGO_URI);
            } catch (ParseException e) {
                //Couldn't read claims
            }
        }
        return null;
    }
}

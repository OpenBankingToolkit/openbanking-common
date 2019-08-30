/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.services.am;

import com.forgerock.openbanking.auth.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.auth.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.auth.model.error.OBRIErrorType;
import com.forgerock.openbanking.auth.services.aspsp.AMASPSPGateway;
import com.forgerock.openbanking.auth.services.aspsp.AMGateway;
import com.forgerock.openbanking.auth.services.aspsp.AMMatlsASPSPGateway;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@Slf4j
public class AMGatewayService {

    @Autowired
    private AMASPSPGateway amGateway;
    @Autowired
    private AMMatlsASPSPGateway amMatlsGateway;
    @Value("${am.matls-hostname}")
    private String amMatlsHostname;

    public AMGateway getAmGateway(String jwt) throws OBErrorResponseException {
        String tokenEndpoint = "https://" + amMatlsHostname + "/oauth2/access_token";
        log.debug("Verify the audience from the jwt {} is equal to the token endpoint {}", jwt, tokenEndpoint);
        try {
            SignedJWT jws = (SignedJWT) JWTParser.parse(jwt);
            if (jws.getJWTClaimsSet().getAudience() != null && jws.getJWTClaimsSet().getAudience().contains(tokenEndpoint)) {
                log.debug("Found the token endpoint as audience => Use the MATLS gateway");
                return this.amMatlsGateway;
            }
        } catch (ParseException e) {
            log.error("Parse JWT error", e);
            throw new OBErrorResponseException(
                    OBRIErrorType.ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID.getHttpStatus(),
                    OBRIErrorResponseCategory.ACCESS_TOKEN,
                    OBRIErrorType.ACCESS_TOKEN_CLIENT_ASSERTION_FORMAT_INVALID.toOBError1(jwt));
        }
        log.debug("The token endpoint wasnt used as audience => Use the non-MATLS gateway");
        return this.amGateway;
    }
}

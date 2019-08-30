/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.claim;

import net.minidev.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Models OpenID Connect claims that are requested in an authorize request.
 */
public class Claims {

    private final Map<String, Claim> userInfoClaims;
    private final Map<String, Claim> idTokenClaims;

    /**
     * Creates a Claims object.
     *
     * @param userInfoClaims The userinfo claims.
     * @param idTokenClaims  The id_token claims.
     */
    public Claims(Map<String, Claim> userInfoClaims, Map<String, Claim> idTokenClaims) {
        this.userInfoClaims = userInfoClaims;
        this.idTokenClaims = idTokenClaims;
    }

    /**
     * Gets the userinfo claims.
     *
     * @return The userinfo claims.
     */
    public Map<String, Claim> getUserInfoClaims() {
        return userInfoClaims;
    }

    /**
     * Gets the id_token claims.
     *
     * @return The id_token claims.
     */
    public Map<String, Claim> getIdTokenClaims() {
        return idTokenClaims;
    }

    /**
     * Gets all the claims for both userinfo and id_token claims.
     *
     * @return All claims.
     */
    public Map<String, Claim> getAllClaims() {
        Map<String, Claim> claims = new HashMap<>(userInfoClaims);
        claims.putAll(idTokenClaims);
        return claims;
    }

    public static Claims parseClaims(JSONObject claimsJson) {
        return new Claims(parseClaimsFrom("user_info", claimsJson), parseClaimsFrom("id_token", claimsJson));
    }

    public static Map<String, Claim> parseClaimsFrom(String key, JSONObject json) {
        Map<String, Claim> claims = new HashMap<>();
        if (json.containsKey(key)) {
            JSONObject claimsAsJson = (JSONObject) json.get(key);
            for (Object claimKey : claimsAsJson.keySet()) {
                claims.put((String) claimKey, Claim.parseClaim((JSONObject) claimsAsJson.get(claimKey)));
            }
        }
        return claims;
    }
}

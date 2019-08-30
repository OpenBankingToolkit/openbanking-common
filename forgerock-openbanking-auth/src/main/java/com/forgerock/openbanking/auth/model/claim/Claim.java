/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.claim;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Claim model, according to the section 5 of the openid connect standard.
 */
public class Claim {

    private boolean essential;
    private List<String> values;

    public Claim(boolean essential, String... values) {
        this.essential = essential;
        this.values = Arrays.asList(values);
    }

    public Claim(boolean essential, List<String> values) {
        this.essential = essential;
        this.values = values;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (values.size() == 1) {
            json.put("value", values.get(0));
        } else if (values.size() > 1) {
            JSONArray jsonValues = new JSONArray();
            jsonValues.addAll(values);
            json.put("values", jsonValues);
        }
        json.put("essential", essential);
        return json;
    }

    public boolean isEssential() {
        return essential;
    }

    public List<String> getValues() {
        return values;
    }

    public String getValue() {
        return values.get(0);
    }

    public static Claim parseClaim(JSONObject json) {
        boolean essential = false;
        List<String> values = new ArrayList<>();
        if (json.containsKey("essential")) {
            essential = (boolean) json.get("essential");
        }
        if (json.containsKey("value")) {
            values.add((String) json.get("value"));
        }
        if (json.containsKey("values")) {
            JSONArray array = (JSONArray) json.get("values");
            String[] valuesAsArray = array.toArray(new String[0]);
            values.addAll(Arrays.asList(valuesAsArray));
        }
        return new Claim(essential, values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Claim)) return false;
        Claim claim = (Claim) o;
        return essential == claim.essential &&
                Objects.equals(values, claim.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(essential, values);
    }
}

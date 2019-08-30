/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;


@Document
public class SoftwareStatement {
    @Id
    private String id;
    private String name;
    private String description;
    private String uri;
    private String version;
    private String environment;
    private String logoUri;
    private Mode mode;
    private Set<SoftwareStatementRole> roles;
    private Status status;

    private String policyUri;
    private String termsOfService;
    private List<String> redirectUris;
    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    private String applicationId;

    public enum Mode {
        TEST, LIVE
    }

    public enum Status {
        ACTIVE, REVOKED
    }

    public SoftwareStatement() {
        status = Status.ACTIVE;

        roles = new HashSet<>();
        roles.add(SoftwareStatementRole.AISP);
        roles.add(SoftwareStatementRole.PISP);
        roles.add(SoftwareStatementRole.CBPII);
        roles.add(SoftwareStatementRole.DATA);

        redirectUris = new ArrayList<>();
        mode = Mode.TEST;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Set<SoftwareStatementRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SoftwareStatementRole> roles) {
        this.roles = roles;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPolicyUri() {
        return policyUri;
    }

    public void setPolicyUri(String policyUri) {
        this.policyUri = policyUri;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}

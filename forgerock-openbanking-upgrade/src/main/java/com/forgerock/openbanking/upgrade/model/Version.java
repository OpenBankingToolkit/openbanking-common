/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.upgrade.model;

import java.util.Objects;

public class Version implements Comparable<Version>{

    public String version;

    public Version(String version) {
        if(version == null)
            throw new IllegalArgumentException("EntitiesVersion can not be null");
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version1 = (Version) o;
        return Objects.equals(version, version1.version);
    }

    @Override
    public int hashCode() {

        return Objects.hash(version);
    }

    @Override
    public int compareTo(Version that) {
        if(that == null)
            return 1;
        String[] thisVersionName = this.version.split("-");
        String[] thatVersionName = that.version.split("-");

        String thisReleaseName = thisVersionName[0];
        String thatReleaseName = thatVersionName[0];
        if (thisReleaseName.matches("[0-9]+(\\.[0-9]+)*") && thatReleaseName.matches("[0-9]+(\\.[0-9]+)*")) {
            //The old format
            String[] thisParts = thisReleaseName.split("\\.");
            String[] thatParts = thatReleaseName.split("\\.");
            int length = Math.max(thisParts.length, thatParts.length);
            for(int i = 0; i < length; i++) {
                int thisPart = i < thisParts.length ?
                        Integer.parseInt(thisParts[i]) : 0;
                int thatPart = i < thatParts.length ?
                        Integer.parseInt(thatParts[i]) : 0;
                if(thisPart < thatPart)
                    return -1;
                if(thisPart > thatPart)
                    return 1;
            }
            return 0;
        }
        if (thisReleaseName.matches("[0-9]+(\\.[0-9]+)*")) {
            return -1;
        }
        if (thatReleaseName.matches("[0-9]+(\\.[0-9]+)*")) {
            return 1;
        }

        if (thisReleaseName.charAt(0) == thatReleaseName.charAt(0)) {
            return 0;
        }
        if (thisReleaseName.charAt(0) < thatReleaseName.charAt(0)) {
            return -1;
        }
        if (thisReleaseName.charAt(0) > thatReleaseName.charAt(0)) {
            return 1;
        }
        //TODO we need to implement what we do once we reach 26 letters of release name
        return 0;
    }

    @Override
    public String toString() {
        return "Version{" +
                "version='" + version + '\'' +
                '}';
    }
}

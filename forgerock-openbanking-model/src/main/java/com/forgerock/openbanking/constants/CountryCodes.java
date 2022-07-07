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
package com.forgerock.openbanking.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CountryCodes {
    private static Map<String, String> twoLetterToThreeLetterCodes;
    private static Map<String, String> threeLetterToTwoLetterCodes;
    static {
        twoLetterToThreeLetterCodes = new HashMap<>();
        twoLetterToThreeLetterCodes.put("AX","ALA");
        twoLetterToThreeLetterCodes.put("AL","ALB");
        twoLetterToThreeLetterCodes.put("DZ","DZA");
        twoLetterToThreeLetterCodes.put("AS","ASM");
        twoLetterToThreeLetterCodes.put("AD","AND");
        twoLetterToThreeLetterCodes.put("AO","AGO");
        twoLetterToThreeLetterCodes.put("AI","AIA");
        twoLetterToThreeLetterCodes.put("AQ","ATA");
        twoLetterToThreeLetterCodes.put("AG","ATG");
        twoLetterToThreeLetterCodes.put("AR","ARG");
        twoLetterToThreeLetterCodes.put("AM","ARM");
        twoLetterToThreeLetterCodes.put("AW","ABW");
        twoLetterToThreeLetterCodes.put("AU","AUS");
        twoLetterToThreeLetterCodes.put("AT","AUT");
        twoLetterToThreeLetterCodes.put("AZ","AZE");
        twoLetterToThreeLetterCodes.put("BS","BHS");
        twoLetterToThreeLetterCodes.put("BH","BHR");
        twoLetterToThreeLetterCodes.put("BD","BGD");
        twoLetterToThreeLetterCodes.put("BB","BRB");
        twoLetterToThreeLetterCodes.put("BY","BLR");
        twoLetterToThreeLetterCodes.put("BE","BEL");
        twoLetterToThreeLetterCodes.put("BZ","BLZ");
        twoLetterToThreeLetterCodes.put("BJ","BEN");
        twoLetterToThreeLetterCodes.put("BM","BMU");
        twoLetterToThreeLetterCodes.put("BT","BTN");
        twoLetterToThreeLetterCodes.put("BO","BOL");
        twoLetterToThreeLetterCodes.put("BA","BIH");
        twoLetterToThreeLetterCodes.put("BW","BWA");
        twoLetterToThreeLetterCodes.put("BV","BVT");
        twoLetterToThreeLetterCodes.put("BR","BRA");
        twoLetterToThreeLetterCodes.put("IO","IOT");
        twoLetterToThreeLetterCodes.put("BN","BRN");
        twoLetterToThreeLetterCodes.put("BG","BGR");
        twoLetterToThreeLetterCodes.put("BF","BFA");
        twoLetterToThreeLetterCodes.put("BI","BDI");
        twoLetterToThreeLetterCodes.put("KH","KHM");
        twoLetterToThreeLetterCodes.put("CM","CMR");
        twoLetterToThreeLetterCodes.put("CA","CAN");
        twoLetterToThreeLetterCodes.put("CV","CPV");
        twoLetterToThreeLetterCodes.put("KY","CYM");
        twoLetterToThreeLetterCodes.put("CF","CAF");
        twoLetterToThreeLetterCodes.put("TD","TCD");
        twoLetterToThreeLetterCodes.put("CL","CHL");
        twoLetterToThreeLetterCodes.put("CN","CHN");
        twoLetterToThreeLetterCodes.put("CX","CXR");
        twoLetterToThreeLetterCodes.put("CC","CCK");
        twoLetterToThreeLetterCodes.put("CO","COL");
        twoLetterToThreeLetterCodes.put("KM","COM");
        twoLetterToThreeLetterCodes.put("CG","COG");
        twoLetterToThreeLetterCodes.put("CD","COD");
        twoLetterToThreeLetterCodes.put("CK","COK");
        twoLetterToThreeLetterCodes.put("CR","CRI");
        twoLetterToThreeLetterCodes.put("CI","CIV");
        twoLetterToThreeLetterCodes.put("HR","HRV");
        twoLetterToThreeLetterCodes.put("CU","CUB");
        twoLetterToThreeLetterCodes.put("CY","CYP");
        twoLetterToThreeLetterCodes.put("CZ","CZE");
        twoLetterToThreeLetterCodes.put("DK","DEN");
        twoLetterToThreeLetterCodes.put("DJ","DJI");
        twoLetterToThreeLetterCodes.put("DM","DMA");
        twoLetterToThreeLetterCodes.put("DO","DOM");
        twoLetterToThreeLetterCodes.put("EC","ECU");
        twoLetterToThreeLetterCodes.put("EG","EGY");
        twoLetterToThreeLetterCodes.put("SV","SLV");
        twoLetterToThreeLetterCodes.put("GQ","GNQ");
        twoLetterToThreeLetterCodes.put("ER","ERI");
        twoLetterToThreeLetterCodes.put("EE","EST");
        twoLetterToThreeLetterCodes.put("ET","ETH");
        twoLetterToThreeLetterCodes.put("FK","FLK");
        twoLetterToThreeLetterCodes.put("FO","FRO");
        twoLetterToThreeLetterCodes.put("FJ","FJI");
        twoLetterToThreeLetterCodes.put("FI","FIN");
        twoLetterToThreeLetterCodes.put("FR","FRA");
        twoLetterToThreeLetterCodes.put("GF","GUF");
        twoLetterToThreeLetterCodes.put("PF","PYF");
        twoLetterToThreeLetterCodes.put("TF","ATF");
        twoLetterToThreeLetterCodes.put("GA","GAB");
        twoLetterToThreeLetterCodes.put("GM","GMB");
        twoLetterToThreeLetterCodes.put("GE","GEO");
        twoLetterToThreeLetterCodes.put("DE","DEU");
        twoLetterToThreeLetterCodes.put("GH","GHA");
        twoLetterToThreeLetterCodes.put("GI","GIB");
        twoLetterToThreeLetterCodes.put("GR","GRC");
        twoLetterToThreeLetterCodes.put("GL","GRL");
        twoLetterToThreeLetterCodes.put("GD","GRD");
        twoLetterToThreeLetterCodes.put("GP","GLP");
        twoLetterToThreeLetterCodes.put("GU","GUM");
        twoLetterToThreeLetterCodes.put("GT","GTM");
        twoLetterToThreeLetterCodes.put("GG","GGY");
        twoLetterToThreeLetterCodes.put("GN","GIN");
        twoLetterToThreeLetterCodes.put("GW","GNB");
        twoLetterToThreeLetterCodes.put("GY","GUY");
        twoLetterToThreeLetterCodes.put("HT","HTI");
        twoLetterToThreeLetterCodes.put("HM","HMD");
        twoLetterToThreeLetterCodes.put("VA","VAT");
        twoLetterToThreeLetterCodes.put("HN","HND");
        twoLetterToThreeLetterCodes.put("HK","HKG");
        twoLetterToThreeLetterCodes.put("HU","HUN");
        twoLetterToThreeLetterCodes.put("IS","ISL");
        twoLetterToThreeLetterCodes.put("IN","IND");
        twoLetterToThreeLetterCodes.put("ID","IDN");
        twoLetterToThreeLetterCodes.put("IR","IRN");
        twoLetterToThreeLetterCodes.put("IQ","IRQ");
        twoLetterToThreeLetterCodes.put("IE","IRL");
        twoLetterToThreeLetterCodes.put("IM","IMN");
        twoLetterToThreeLetterCodes.put("IL","ISR");
        twoLetterToThreeLetterCodes.put("IT","ITA");
        twoLetterToThreeLetterCodes.put("JM","JAM");
        twoLetterToThreeLetterCodes.put("JP","JPN");
        twoLetterToThreeLetterCodes.put("JE","JEY");
        twoLetterToThreeLetterCodes.put("JO","JOR");
        twoLetterToThreeLetterCodes.put("KZ","KAZ");
        twoLetterToThreeLetterCodes.put("KE","KEN");
        twoLetterToThreeLetterCodes.put("KI","KIR");
        twoLetterToThreeLetterCodes.put("KP","PRK");
        twoLetterToThreeLetterCodes.put("KR","KOR");
        twoLetterToThreeLetterCodes.put("KW","KWT");
        twoLetterToThreeLetterCodes.put("KG","KGZ");
        twoLetterToThreeLetterCodes.put("LA","LAO");
        twoLetterToThreeLetterCodes.put("LV","LAT");
        twoLetterToThreeLetterCodes.put("LB","LBN");
        twoLetterToThreeLetterCodes.put("LS","LSO");
        twoLetterToThreeLetterCodes.put("LR","LBR");
        twoLetterToThreeLetterCodes.put("LY","LBY");
        twoLetterToThreeLetterCodes.put("LI","LIE");
        twoLetterToThreeLetterCodes.put("LT","LTU");
        twoLetterToThreeLetterCodes.put("LU","LUX");
        twoLetterToThreeLetterCodes.put("MO","MAC");
        twoLetterToThreeLetterCodes.put("MK","MKD");
        twoLetterToThreeLetterCodes.put("MG","MDG");
        twoLetterToThreeLetterCodes.put("MW","MWI");
        twoLetterToThreeLetterCodes.put("MY","MYS");
        twoLetterToThreeLetterCodes.put("MV","MDV");
        twoLetterToThreeLetterCodes.put("ML","MLI");
        twoLetterToThreeLetterCodes.put("MT","MLT");
        twoLetterToThreeLetterCodes.put("MH","MHL");
        twoLetterToThreeLetterCodes.put("MQ","MTQ");
        twoLetterToThreeLetterCodes.put("MR","MRT");
        twoLetterToThreeLetterCodes.put("MU","MUS");
        twoLetterToThreeLetterCodes.put("YT","MYT");
        twoLetterToThreeLetterCodes.put("MX","MEX");
        twoLetterToThreeLetterCodes.put("FM","FSM");
        twoLetterToThreeLetterCodes.put("MD","MDA");
        twoLetterToThreeLetterCodes.put("MC","MCO");
        twoLetterToThreeLetterCodes.put("MN","MNG");
        twoLetterToThreeLetterCodes.put("ME","MNE");
        twoLetterToThreeLetterCodes.put("MS","MSR");
        twoLetterToThreeLetterCodes.put("MA","MAR");
        twoLetterToThreeLetterCodes.put("MZ","MOZ");
        twoLetterToThreeLetterCodes.put("MM","MMR");
        twoLetterToThreeLetterCodes.put("NA","NAM");
        twoLetterToThreeLetterCodes.put("NR","NRU");
        twoLetterToThreeLetterCodes.put("NP","NPL");
        twoLetterToThreeLetterCodes.put("NL","NLD");
        twoLetterToThreeLetterCodes.put("AN","ANT");
        twoLetterToThreeLetterCodes.put("NC","NCL");
        twoLetterToThreeLetterCodes.put("NZ","NZL");
        twoLetterToThreeLetterCodes.put("NI","NIC");
        twoLetterToThreeLetterCodes.put("NE","NER");
        twoLetterToThreeLetterCodes.put("NG","NGA");
        twoLetterToThreeLetterCodes.put("NU","NIU");
        twoLetterToThreeLetterCodes.put("NF","NFK");
        twoLetterToThreeLetterCodes.put("MP","MNP");
        twoLetterToThreeLetterCodes.put("NO","NOR");
        twoLetterToThreeLetterCodes.put("OM","OMN");
        twoLetterToThreeLetterCodes.put("PK","PAK");
        twoLetterToThreeLetterCodes.put("PW","PLW");
        twoLetterToThreeLetterCodes.put("PS","PSE");
        twoLetterToThreeLetterCodes.put("PA","PAN");
        twoLetterToThreeLetterCodes.put("PG","PNG");
        twoLetterToThreeLetterCodes.put("PY","PRY");
        twoLetterToThreeLetterCodes.put("PE","PER");
        twoLetterToThreeLetterCodes.put("PH","PHL");
        twoLetterToThreeLetterCodes.put("PN","PCN");
        twoLetterToThreeLetterCodes.put("PL","POL");
        twoLetterToThreeLetterCodes.put("PT","PRT");
        twoLetterToThreeLetterCodes.put("PR","PRI");
        twoLetterToThreeLetterCodes.put("QA","QAT");
        twoLetterToThreeLetterCodes.put("RE","REU");
        twoLetterToThreeLetterCodes.put("RO","ROU");
        twoLetterToThreeLetterCodes.put("RU","RUS");
        twoLetterToThreeLetterCodes.put("RW","RWA");
        twoLetterToThreeLetterCodes.put("BL","BLM");
        twoLetterToThreeLetterCodes.put("SH","SHN");
        twoLetterToThreeLetterCodes.put("KN","KNA");
        twoLetterToThreeLetterCodes.put("LC","LCA");
        twoLetterToThreeLetterCodes.put("MF","MAF");
        twoLetterToThreeLetterCodes.put("PM","SPM");
        twoLetterToThreeLetterCodes.put("VC","VCT");
        twoLetterToThreeLetterCodes.put("WS","WSM");
        twoLetterToThreeLetterCodes.put("SM","SMR");
        twoLetterToThreeLetterCodes.put("ST","STP");
        twoLetterToThreeLetterCodes.put("SA","SAU");
        twoLetterToThreeLetterCodes.put("SN","SEN");
        twoLetterToThreeLetterCodes.put("RS","SRB");
        twoLetterToThreeLetterCodes.put("SC","SYC");
        twoLetterToThreeLetterCodes.put("SL","SLE");
        twoLetterToThreeLetterCodes.put("SG","SGP");
        twoLetterToThreeLetterCodes.put("SK","SVK");
        twoLetterToThreeLetterCodes.put("SI","SVN");
        twoLetterToThreeLetterCodes.put("SB","SLB");
        twoLetterToThreeLetterCodes.put("SO","SOM");
        twoLetterToThreeLetterCodes.put("ZA","ZAF");
        twoLetterToThreeLetterCodes.put("GS","SGS");
        twoLetterToThreeLetterCodes.put("ES","ESP");
        twoLetterToThreeLetterCodes.put("LK","LKA");
        twoLetterToThreeLetterCodes.put("SD","SDN");
        twoLetterToThreeLetterCodes.put("SR","SUR");
        twoLetterToThreeLetterCodes.put("SJ","SJM");
        twoLetterToThreeLetterCodes.put("SZ","SWZ");
        twoLetterToThreeLetterCodes.put("SE","SWE");
        twoLetterToThreeLetterCodes.put("CH","CHE");
        twoLetterToThreeLetterCodes.put("SY","SYR");
        twoLetterToThreeLetterCodes.put("TW","TWN");
        twoLetterToThreeLetterCodes.put("TJ","TJK");
        twoLetterToThreeLetterCodes.put("TZ","TZA");
        twoLetterToThreeLetterCodes.put("TH","THA");
        twoLetterToThreeLetterCodes.put("TL","TLS");
        twoLetterToThreeLetterCodes.put("TG","TGO");
        twoLetterToThreeLetterCodes.put("TK","TKL");
        twoLetterToThreeLetterCodes.put("TO","TON");
        twoLetterToThreeLetterCodes.put("TT","TTO");
        twoLetterToThreeLetterCodes.put("TN","TUN");
        twoLetterToThreeLetterCodes.put("TR","TUR");
        twoLetterToThreeLetterCodes.put("TM","TKM");
        twoLetterToThreeLetterCodes.put("TC","TCA");
        twoLetterToThreeLetterCodes.put("TV","TUV");
        twoLetterToThreeLetterCodes.put("UG","UGA");
        twoLetterToThreeLetterCodes.put("UA","UKR");
        twoLetterToThreeLetterCodes.put("AE","ARE");
        twoLetterToThreeLetterCodes.put("GB","GBR");
        twoLetterToThreeLetterCodes.put("US","USA");
        twoLetterToThreeLetterCodes.put("UM","UMI");
        twoLetterToThreeLetterCodes.put("UY","URY");
        twoLetterToThreeLetterCodes.put("UZ","UZB");
        twoLetterToThreeLetterCodes.put("VU","VUT");
        twoLetterToThreeLetterCodes.put("VE","VEN");
        twoLetterToThreeLetterCodes.put("VN","VNM");
        twoLetterToThreeLetterCodes.put("VG","VGB");
        twoLetterToThreeLetterCodes.put("VI","VIR");
        twoLetterToThreeLetterCodes.put("WF","WLF");
        twoLetterToThreeLetterCodes.put("EH","ESH");
        twoLetterToThreeLetterCodes.put("YE","YEM");
        twoLetterToThreeLetterCodes.put("ZM","ZMB");
        twoLetterToThreeLetterCodes.put("ZW","ZWE");
        threeLetterToTwoLetterCodes = new HashMap<>();
        threeLetterToTwoLetterCodes = twoLetterToThreeLetterCodes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue,
                Map.Entry::getKey));

    }

    public static String getTwoLetterCountryCodeFromThreeLetterCountryCode(String threeLetterCountryCode){
        return threeLetterToTwoLetterCodes.get(threeLetterCountryCode);
    }

    public static String getThreeLetterCountryCodeFromTwoLetterCountryCode(String twoLetterCountryCode){
        return twoLetterToThreeLetterCodes.get(twoLetterCountryCode);
    }
}

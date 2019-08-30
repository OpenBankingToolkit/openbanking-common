/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.auth.model.error;

public class ErrorResponseTmp {

    private String message = "";
    private int code = -1;

    private ErrorResponseTmp(int code) {
        this.code = code;
    }

    private ErrorResponseTmp(String errorMessage, int code) {
        this.message = errorMessage;
        this.code = code;
    }

    public static ErrorResponseTmp code(int code) {
        return new ErrorResponseTmp(code);
    }

    public ErrorResponseTmp message(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }


    @Override
    public String toString() {
        return "ErrorResponseTmp{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}

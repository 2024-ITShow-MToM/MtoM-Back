package com.MtoM.MtoM.global.message;

public enum ResponseMessageType {
    SUCCESS_CREATE("Successfully created the value."),
    SUCCESS_UPDATE("Successfully updated the value."),
    SUCCESS_DELETE("Successfully deleted the value.");

    private final String message;

    ResponseMessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
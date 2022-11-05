package edu.rice.comp610.util;

import java.util.Map;

public class BadRequestException extends Exception{
    private final Map<String, String> requestErrors;

    public BadRequestException(String message, Map<String, String> requestErrors) {
        super(message);
        this.requestErrors = requestErrors;
    }

    public Map<String, String> getRequestErrors() {
        return requestErrors;
    }
}

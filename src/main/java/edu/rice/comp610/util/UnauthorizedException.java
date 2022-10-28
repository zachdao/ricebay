package edu.rice.comp610.util;

public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
        super("Unauthorized");
    }
}

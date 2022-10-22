package edu.rice.comp610.store;

/**
 * Thrown when the requested user account is not found.
 */
public class ObjectNotFoundException extends Exception {

    public ObjectNotFoundException(String message) {
        super(message);
    }
}

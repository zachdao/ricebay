package edu.rice.comp610.store;

/**
 * An Exception generated in the {@link DatabaseManager}
 */
public class DatabaseException extends Exception {

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
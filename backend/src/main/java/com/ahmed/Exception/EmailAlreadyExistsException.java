package com.ahmed.Exception;

/**
 * Thrown when an attempt is made to create (or update) a user
 * with an email address that already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L; // keeps IDEs quiet about serialization

    public EmailAlreadyExistsException() {
        super("Email already in use");
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

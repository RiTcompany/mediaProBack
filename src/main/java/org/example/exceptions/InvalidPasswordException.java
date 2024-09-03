package org.example.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String username) {
        super("Invalid password for user: " + username);
    }
}

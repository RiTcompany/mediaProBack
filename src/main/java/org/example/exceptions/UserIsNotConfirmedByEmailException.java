package org.example.exceptions;

public class UserIsNotConfirmedByEmailException extends RuntimeException {
    public UserIsNotConfirmedByEmailException(String email) {
        super("User is not confirmed by email: " + email);
    }
}

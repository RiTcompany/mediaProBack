package org.example.exceptions;

public class AuthHeaderFoundException extends RuntimeException {
    public AuthHeaderFoundException(String msg) {
        super(msg);
    }
}

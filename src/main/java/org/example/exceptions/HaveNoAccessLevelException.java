package org.example.exceptions;

public class HaveNoAccessLevelException extends RuntimeException {
    public HaveNoAccessLevelException(String role) {
        super("Have no permission to this resource by role: " + role);
    }
}

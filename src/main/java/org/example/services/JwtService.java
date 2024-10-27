package org.example.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.SignatureException;

public interface JwtService {
    String extractUserName(String token) throws SignatureException;

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails) throws SignatureException;
}

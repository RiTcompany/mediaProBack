package org.example.services;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface MobileFileService {
    byte[] getStaticFile(String file) throws IOException;
}

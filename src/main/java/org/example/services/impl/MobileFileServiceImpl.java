package org.example.services.impl;

import org.example.services.MobileFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class MobileFileServiceImpl implements MobileFileService {
    @Value("${static.source}")
    private String staticSource;

    @Override
    public byte[] getStaticFile(String file) throws IOException {
        if (Objects.equals(file, "apple-app-site-association")) file = file+".json";
        return  Files.readAllBytes(Paths.get(staticSource.concat(file)));
    }
}

package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.MobileFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MobileFileController {
    private final MobileFileService mobileFileService;

    @GetMapping("/.well-known/{file}")
    public ResponseEntity<byte[]> getPracticeTask(@PathVariable("file") String file) throws IOException {
        return ResponseEntity.ok(mobileFileService.getStaticFile(file));
    }
}

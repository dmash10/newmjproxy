package com.github.novicezk.midjourney.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StaticController {

    @GetMapping("/static/{filename:.+}")
    public ResponseEntity<Resource> serveStatic(@PathVariable String filename) {
        return serveFile(filename);
    }

    @GetMapping("/{filename:.+\\.js}")
    public ResponseEntity<Resource> serveJs(@PathVariable String filename) {
        return serveFile(filename);
    }

    @GetMapping("/{filename:.+\\.css}")
    public ResponseEntity<Resource> serveCss(@PathVariable String filename) {
        return serveFile(filename);
    }

    private ResponseEntity<Resource> serveFile(String filename) {
        try {
            Resource resource = new ClassPathResource("static/" + filename);
            if (resource.exists() && resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                
                // Set content type based on extension
                if (filename.endsWith(".js")) {
                    headers.add("Content-Type", "application/javascript");
                } else if (filename.endsWith(".css")) {
                    headers.add("Content-Type", "text/css");
                } else if (filename.endsWith(".html")) {
                    headers.add("Content-Type", "text/html");
                }
                
                headers.add("Cache-Control", "no-cache");
                
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            }
        } catch (Exception e) {
            // Ignore and return 404
        }
        return ResponseEntity.notFound().build();
    }
} 
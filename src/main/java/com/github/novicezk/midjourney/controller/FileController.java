package com.github.novicezk.midjourney.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileController {

    @GetMapping(value = {
        "/*.js", "/*.css", "/*.html", "/*.png", "/*.webp", "/*.ico", "/*.svg",
        "/scripts/*.js", "/static/**"
    })
    public ResponseEntity<byte[]> serveFile(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Remove context path
        if (path.startsWith("/mj")) {
            path = path.substring(3);
        }
        
        // Remove leading slash
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        try {
            ClassPathResource resource = new ClassPathResource("static/" + path);
            if (resource.exists()) {
                InputStream inputStream = resource.getInputStream();
                byte[] content = inputStream.readAllBytes();
                inputStream.close();
                
                HttpHeaders headers = new HttpHeaders();
                
                // Set content type
                if (path.endsWith(".js")) {
                    headers.add("Content-Type", "application/javascript; charset=utf-8");
                } else if (path.endsWith(".css")) {
                    headers.add("Content-Type", "text/css; charset=utf-8");
                } else if (path.endsWith(".html")) {
                    headers.add("Content-Type", "text/html; charset=utf-8");
                } else if (path.endsWith(".png")) {
                    headers.add("Content-Type", "image/png");
                } else if (path.endsWith(".webp")) {
                    headers.add("Content-Type", "image/webp");
                } else if (path.endsWith(".ico")) {
                    headers.add("Content-Type", "image/x-icon");
                } else if (path.endsWith(".svg")) {
                    headers.add("Content-Type", "image/svg+xml");
                }
                
                headers.add("Cache-Control", "no-cache");
                headers.add("Access-Control-Allow-Origin", "*");
                
                return new ResponseEntity<>(content, headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            // File not found or error reading
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
} 
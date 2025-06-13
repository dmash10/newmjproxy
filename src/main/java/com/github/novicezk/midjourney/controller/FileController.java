package com.github.novicezk.midjourney.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@RestController
public class FileController {

    @RequestMapping("/**")
    public ResponseEntity<byte[]> handleAll(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Remove context path
        if (path.startsWith("/mj")) {
            path = path.substring(3);
        }
        
        // Remove leading slash
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        // Only handle static file extensions
        if (isStaticFile(path)) {
            try {
                ClassPathResource resource = new ClassPathResource("static/" + path);
                if (resource.exists()) {
                    InputStream inputStream = resource.getInputStream();
                    byte[] content = inputStream.readAllBytes();
                    inputStream.close();
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Type", getContentType(path));
                    headers.add("Cache-Control", "no-cache");
                    headers.add("Access-Control-Allow-Origin", "*");
                    
                    return new ResponseEntity<>(content, headers, HttpStatus.OK);
                }
            } catch (Exception e) {
                // Fall through to 404
            }
        }
        
        // Not a static file or not found - let other controllers handle it
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    private boolean isStaticFile(String path) {
        return path.endsWith(".js") || path.endsWith(".css") || path.endsWith(".html") ||
               path.endsWith(".png") || path.endsWith(".webp") || path.endsWith(".ico") ||
               path.endsWith(".svg") || path.contains(".");
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (path.endsWith(".css")) return "text/css; charset=utf-8";
        if (path.endsWith(".html")) return "text/html; charset=utf-8";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".webp")) return "image/webp";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
} 
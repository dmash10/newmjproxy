package com.github.novicezk.midjourney.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class StaticFileController {

    @GetMapping("/**/*.js")
    public ResponseEntity<Resource> serveJavaScript(HttpServletRequest request) {
        return serveStaticFile(request, "application/javascript");
    }

    @GetMapping("/**/*.css")
    public ResponseEntity<Resource> serveStylesheet(HttpServletRequest request) {
        return serveStaticFile(request, "text/css");
    }

    @GetMapping("/**/*.html")
    public ResponseEntity<Resource> serveHtml(HttpServletRequest request) {
        return serveStaticFile(request, "text/html");
    }

    @GetMapping(value = {"/**/*.png", "/**/*.webp", "/**/*.ico"})
    public ResponseEntity<Resource> serveImage(HttpServletRequest request) {
        return serveStaticFile(request, "image/png");
    }

    private ResponseEntity<Resource> serveStaticFile(HttpServletRequest request, String contentType) {
        String path = request.getRequestURI();
        
        // Remove context path if present
        if (path.startsWith("/mj")) {
            path = path.substring(3);
        }
        
        // Remove leading slash
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        System.out.println("🔍 [STATIC] Serving file: " + path);
        
        try {
            Resource resource = new ClassPathResource("static/" + path);
            if (resource.exists() && resource.isReadable()) {
                System.out.println("✅ [STATIC] Found file: " + path);
                
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", contentType);
                headers.add("Cache-Control", "no-cache");
                
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                System.out.println("❌ [STATIC] File not found: " + path);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("💥 [STATIC] Error serving file: " + path + " - " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
} 
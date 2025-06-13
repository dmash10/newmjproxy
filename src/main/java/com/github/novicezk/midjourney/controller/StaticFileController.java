package com.github.novicezk.midjourney.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class StaticFileController {

    @GetMapping("/**/*.js")
    public ResponseEntity<Resource> serveJavaScript(HttpServletRequest request) {
        return serveStaticFile(request, MediaType.APPLICATION_JSON); // Use JSON as fallback
    }

    @GetMapping("/**/*.css")
    public ResponseEntity<Resource> serveStylesheet(HttpServletRequest request) {
        return serveStaticFile(request, MediaType.TEXT_CSS);
    }

    @GetMapping("/**/*.html")
    public ResponseEntity<Resource> serveHtml(HttpServletRequest request) {
        return serveStaticFile(request, MediaType.TEXT_HTML);
    }

    @GetMapping(value = {"/**/*.png", "/**/*.webp", "/**/*.ico"})
    public ResponseEntity<Resource> serveImage(HttpServletRequest request) {
        return serveStaticFile(request, MediaType.APPLICATION_OCTET_STREAM);
    }

    private ResponseEntity<Resource> serveStaticFile(HttpServletRequest request, MediaType mediaType) {
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
                headers.setContentType(mediaType);
                headers.setCacheControl("no-cache"); // Disable cache for debugging
                
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
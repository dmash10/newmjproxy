package com.github.novicezk.midjourney.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@RestController
public class FileController {

    // Regex: matches any path ending with a segment containing at least one character, a dot, and at least one character.
    // This ensures we only catch requests for actual files like name.ext
    // It will not match /admin, /admin/, etc.
    // Corrected regex: \. needs to be \\. in Java string
    @RequestMapping(value = "/**/{filename:[^/]+\\.[^/]+}")
    public ResponseEntity<byte[]> serveFile(HttpServletRequest request, @PathVariable String filename) {
        String originalRequestPath = request.getRequestURI();
        String pathInStaticDir;

        // Remove /mj context path
        if (originalRequestPath.startsWith("/mj/")) {
            pathInStaticDir = originalRequestPath.substring(4);
        } else if (originalRequestPath.startsWith("/")) {
            pathInStaticDir = originalRequestPath.substring(1); // Should typically not happen with /mj
        } else {
            pathInStaticDir = originalRequestPath; // Should also not happen
        }

        // The @PathVariable filename is just the last segment. pathInStaticDir is the full relative path.
        // e.g. for /mj/scripts/foo.js, filename="foo.js", pathInStaticDir="scripts/foo.js"

        try {
            ClassPathResource resource = new ClassPathResource("static/" + pathInStaticDir);
            if (resource.exists() && resource.isReadable()) {
                InputStream inputStream = resource.getInputStream();
                byte[] content = inputStream.readAllBytes();
                inputStream.close();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", getContentType(pathInStaticDir));
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                headers.add("Access-Control-Allow-Origin", "*");

                return new ResponseEntity<>(content, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            // Log error or ignore, then fall through to NOT_FOUND
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private String getContentType(String path) {
        if (path.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (path.endsWith(".css")) return "text/css; charset=utf-8";
        if (path.endsWith(".html")) return "text/html; charset=utf-8";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".webp")) return "image/webp";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream"; // Default
    }
} 
package com.github.novicezk.midjourney.controller;

import com.github.novicezk.midjourney.ProxyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {
    
    private final ProxyProperties properties;

    @GetMapping("/auth")
    public Map<String, Object> debugAuth(
            HttpServletRequest request,
            @RequestHeader(value = "mj-api-secret", required = false) String apiSecret) {
        
        Map<String, Object> result = new HashMap<>();
        
        // Check if API secret is configured
        String configuredSecret = properties.getApiSecret();
        result.put("hasConfiguredSecret", configuredSecret != null && !configuredSecret.trim().isEmpty());
        result.put("configuredSecretLength", configuredSecret != null ? configuredSecret.length() : 0);
        
        // Check received header
        result.put("hasReceivedSecret", apiSecret != null && !apiSecret.trim().isEmpty());
        result.put("receivedSecretLength", apiSecret != null ? apiSecret.length() : 0);
        
        // Check if they match
        boolean matches = configuredSecret != null && configuredSecret.equals(apiSecret);
        result.put("secretsMatch", matches);
        
        // Log for debugging
        log.info("🔍 Debug Auth Check:");
        log.info("  - Configured secret: {}", configuredSecret != null ? "[SET]" : "[NOT SET]");
        log.info("  - Received secret: {}", apiSecret != null ? "[PROVIDED]" : "[MISSING]");
        log.info("  - Secrets match: {}", matches);
        
        return result;
    }
    
    @GetMapping("/headers")
    public Map<String, String> debugHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            headers.put(headerName, request.getHeader(headerName));
        });
        
        log.info("🔍 All request headers: {}", headers);
        
        return headers;
    }
} 
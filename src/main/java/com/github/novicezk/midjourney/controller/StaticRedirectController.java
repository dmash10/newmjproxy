package com.github.novicezk.midjourney.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class StaticRedirectController {

    /**
     * Redirect static files that are incorrectly requested from root to the /mj context
     * This handles cases where React build has hardcoded absolute paths
     */
    @GetMapping("/{filename:[0-9]+\\.[a-f0-9]+\\.async\\.js}")
    public void redirectAsyncJs(@PathVariable String filename, HttpServletResponse response) throws IOException {
        response.sendRedirect("/mj/" + filename);
    }

    @GetMapping("/{filename:[0-9]+\\.[a-f0-9]+\\.chunk\\.css}")
    public void redirectChunkCss(@PathVariable String filename, HttpServletResponse response) throws IOException {
        response.sendRedirect("/mj/" + filename);
    }

    @GetMapping("/{filename:p__.*\\.[a-f0-9]+\\.async\\.js}")
    public void redirectPageJs(@PathVariable String filename, HttpServletResponse response) throws IOException {
        response.sendRedirect("/mj/" + filename);
    }

    @GetMapping("/{filename:t__.*\\.[a-f0-9]+\\.async\\.js}")
    public void redirectTemplateJs(@PathVariable String filename, HttpServletResponse response) throws IOException {
        response.sendRedirect("/mj/" + filename);
    }

    @GetMapping("/{filename:t__.*\\.[a-f0-9]+\\.chunk\\.css}")
    public void redirectTemplateCss(@PathVariable String filename, HttpServletResponse response) throws IOException {
        response.sendRedirect("/mj/" + filename);
    }
} 
package com.github.novicezk.midjourney.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebUIController {

    @GetMapping("/admin")
    public String admin() {
        return "forward:/index.html";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/admin";
    }

    // Handle React Router routes
    @RequestMapping(value = {"/admin/**", "/draw", "/task", "/account", "/user", "/setting", "/probe"})
    public String reactRoutes() {
        return "forward:/index.html";
    }
} 
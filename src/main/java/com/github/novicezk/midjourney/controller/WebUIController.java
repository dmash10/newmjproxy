package com.github.novicezk.midjourney.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebUIController {

    @GetMapping("/admin")
    public String admin() {
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/admin";
    }
} 
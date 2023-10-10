package com.example.erp_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "views/auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "views/auth/register";
    }

}
